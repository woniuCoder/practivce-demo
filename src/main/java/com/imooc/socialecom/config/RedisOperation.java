package com.imooc.socialecom.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("all")
@Component
@EnableAsync
public class RedisOperation {

    @Autowired
    private RedisTemplate redisTemplate;

    @Async
    public void asyncRedisStringSet(String key, Object value, Long expireTime, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value, expireTime, timeUnit);
    }

}
