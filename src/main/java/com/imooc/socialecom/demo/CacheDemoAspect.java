package com.imooc.socialecom.demo;

import com.alibaba.fastjson.JSON;
import com.imooc.socialecom.pojo.TGoods;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.stereotype.Component;


@SuppressWarnings("all")
@Aspect
@Component
public class CacheDemoAspect {

    private static final Logger logger = LoggerFactory.getLogger(CacheDemoAspect.class);

    @Autowired
    private RedisTemplate redisTemplate;

    @Around("@annotation(cacheDemo)")
    public Object doAroundCacheDemo(ProceedingJoinPoint joinPoint, CacheDemo cacheDemo) throws Throwable {
        String cacheKey = null;
        //获取方法签名
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
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
        cacheKey = cacheDemo.cacheName() + expression.getValue(evaluationContext).toString();
        Object value = redisTemplate.opsForValue().get(cacheKey);
        logger.info(" get from key : {}, get from value : {}", cacheKey, value);
        if (value != null) {
            return value;
        }
        //执行原插入方法
        value = joinPoint.proceed();
        logger.info("get after key : {}, get after value : {}", cacheKey, value);
        if (cacheDemo.expireInSeconds() <= 0) {
            redisTemplate.opsForValue().set(cacheKey, value);
        } else {
            redisTemplate.opsForValue().set(cacheKey, value, cacheDemo.expireInSeconds());
        }
        return value;
    }


}
