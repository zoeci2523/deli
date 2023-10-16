package com.delicoffee.deli;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("com.delicoffee.deli.mapper")
@SpringBootApplication()
public class DeliApplication {

    public static void main(String[] args) {
        SpringApplication.run(DeliApplication.class, args);
    }

}
