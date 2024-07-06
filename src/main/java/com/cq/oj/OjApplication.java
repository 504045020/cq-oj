package com.cq.oj;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.cq.oj.mapper")
@SpringBootApplication
public class OjApplication {

    public static void main(String[] args) {
        SpringApplication.run(OjApplication.class, args);
    }
}
