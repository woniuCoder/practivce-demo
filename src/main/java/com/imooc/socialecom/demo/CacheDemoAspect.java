package com.imooc.socialecom.demo;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import com.imooc.socialecom.pojo.TGoods;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;


@SuppressWarnings("all")
@Aspect
@Component
@ConfigurationProperties(prefix = "mycacheable.rate.limit")
@Setter
@Getter
@RefreshScope
public class CacheDemoAspect {


    private static final Logger logger = LoggerFactory.getLogger(CacheDemoAspect.class);

    private Map<String, RateLimiter> rateLimiterMap = Maps.newHashMap();

    //key：getGoods value：goodsList
    private Map<String, Double> map;

    @Value("${methodName.findById.demo.key}")
    private String key;
    @Value("${limit.goodsList.demo.value}")
    private String value;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostConstruct
    private void initRateLimiterMap() {
        if (!CollectionUtils.isEmpty(map)) {
            map.forEach((methodName, permits) -> {
                RateLimiter rateLimiter = RateLimiter.create(permits);
                rateLimiterMap.put(methodName, rateLimiter);
            });
        }
    }

    @Around("@annotation(cacheDemo)")
    public Object doAroundCacheDemo(ProceedingJoinPoint joinPoint, CacheDemo cacheDemo) throws Throwable {
        String cacheKey = null;
        //获取方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        //TODO 限流处理
        Method method = methodSignature.getMethod();
        String name = method.getName();
        RateLimiter rateLimiter = rateLimiterMap.get(name);
        //获取令牌
        if (rateLimiter != null) {
            int timeOut = cacheDemo.waitInSeconds();
            if (timeOut <= 0) {
                rateLimiter.acquire();
            } else {
                boolean acquire = rateLimiter.tryAcquire(timeOut, TimeUnit.SECONDS);
                if (!acquire) {
                    throw new RuntimeException("系统繁忙");
                }
            }
        }

        //spring获取参数列表
        DefaultParameterNameDiscoverer discoverer = new DefaultParameterNameDiscoverer();
        //获取参数名列表
        String[] parameterNames = discoverer.getParameterNames(methodSignature.getMethod());
        //获取参数值列表
        Object[] args = joinPoint.getArgs();
        ExpressionParser expressionParser = new SpelExpressionParser();
        Expression expression = expressionParser.parseExpression(cacheDemo.key());
        EvaluationContext evaluationContext = new StandardEvaluationContext();
        for (int i = 0; i < parameterNames.length; i++) {
            evaluationContext.setVariable(parameterNames[i], args[i]);
        }
        cacheKey = cacheDemo.cacheName() + expression.getValue(evaluationContext);
        Object value = redisTemplate.opsForValue().get(cacheKey);
        logger.info(" get from key : {}, get from value : {}", cacheKey, value);
        if (value != null) {
            return JSONObject.parseObject(value.toString(), TGoods.class);
        }

        value = joinPoint.proceed();
        logger.info("get after key : {}, get after value : {}", cacheKey, value);
        if (cacheDemo.expireInSeconds() <= 0) {
            redisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(value));
        } else {
            redisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(value), cacheDemo.expireInSeconds());
        }
        return value;
    }

}
