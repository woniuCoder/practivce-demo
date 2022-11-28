package com.imooc.socialecom;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

@SpringBootApplication
@MapperScan("com.imooc.socialecom.mapper")
@EnableAspectJAutoProxy
@EnableDiscoveryClient
public class SocialecomApplication {

    public static void main(String[] args) {
        SpringApplication.run(SocialecomApplication.class, args);
    }

}