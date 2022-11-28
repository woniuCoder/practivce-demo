package com.imooc.socialecom.demo;

import java.lang.annotation.*;

/**
 * 自定义缓存注解
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheDemo {

    /**
     * 缓存名称前缀
     * {cacheName}:{key}
     *
     * @return
     */
    String cacheName() default "";

    /**
     * 缓存key
     *
     * @return
     */
    String key() default "";

    /**
     * 过期时间
     *
     * @return
     */
    int expireInSeconds() default 0;

    /**
     * 默认超时时间
     *
     * @return
     */
    int waitInSeconds() default 0;

}
