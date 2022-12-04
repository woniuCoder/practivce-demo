package com.imooc.socialecom.demo;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import com.imooc.socialecom.converter.BaseResponse;
import com.imooc.socialecom.enums.ResponseEnum;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@SuppressWarnings("all")
@Aspect
@Component
public class MyRateLimiterAspect {

    private static final Logger logger = LoggerFactory.getLogger(MyRateLimiterAspect.class);

    private Map<String, RateLimiter> rateLimiterMap = Maps.newConcurrentMap();

    @Around("@annotation(myRateLimiterAnnotation)")
    public Object process(ProceedingJoinPoint point, MyRateLimiter myRateLimiterAnnotation) throws Throwable {
        //先获取请求路径拼接限流器
        //然后创建限流器
        //返回拼接结果
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String resource = request.getRequestURI();
        RateLimiter rateLimiter = this.getRateLimiter(resource, myRateLimiterAnnotation);
        boolean acquire = rateLimiter.tryAcquire(myRateLimiterAnnotation.permits(),
                myRateLimiterAnnotation.timeout(),
                myRateLimiterAnnotation.timeUnit());
        if (acquire) {
            // 没有获取到令牌需要限流处理
            logger.warn("{}触发限流限制了！", resource);
            this.outputResponse();
        }
        return point.proceed();
    }

    private RateLimiter getRateLimiter(String resource, MyRateLimiter myRateLimiterAnnotation) {
        RateLimiter rateLimiter = rateLimiterMap.get(resource);
        if (rateLimiter == null) {
            rateLimiterMap.put(resource, createRateLimiter(myRateLimiterAnnotation));
            return rateLimiter;
        }
        return rateLimiter;
    }

    private RateLimiter createRateLimiter(MyRateLimiter myRateLimiterAnnotation) {
        RateLimiter rateLimiter;
        if (myRateLimiterAnnotation.isWarmup()) {
            // 以平滑预热方法创建限流器
            rateLimiter = RateLimiter.create(
                    myRateLimiterAnnotation.permitsPerSecond(),
                    myRateLimiterAnnotation.warmupPeriod(),
                    myRateLimiterAnnotation.warmupTimeUnit()
            );
        } else {
            // 以平滑突发方法创建限流器
            rateLimiter = RateLimiter.create(myRateLimiterAnnotation.permitsPerSecond());
        }
        return rateLimiter;
    }

    private void outputResponse() {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        response.setContentType("application/json; charset=utf-8");
        response.setCharacterEncoding("UTF-8");
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            BaseResponse baseResponse = new BaseResponse(ResponseEnum.SYSTEM_BUSY);
            outputStream.write(JSON.toJSONString(baseResponse).getBytes(StandardCharsets.UTF_8));
            outputStream.flush();
        } catch (Exception e) {
            logger.error("输出响应异常", e);
        }
    }

}
