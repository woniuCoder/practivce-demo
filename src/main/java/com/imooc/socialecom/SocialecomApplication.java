package com.imooc.socialecom;

import com.imooc.socialecom.config.IDGenerator;
import com.imooc.socialecom.converter.Properties;
import com.imooc.socialecom.enums.IDTypeEnum;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;

@SpringBootApplication
@MapperScan("com.imooc.socialecom.mapper")
@EnableAspectJAutoProxy
public class SocialecomApplication implements ApplicationRunner {

    public static void main(String[] args) {
        SpringApplication.run(SocialecomApplication.class, args);
    }

    @Autowired
    private IDGenerator idGenerator;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("打印分布式Id" + idGenerator.incr(IDTypeEnum.COUPON));
    }


}