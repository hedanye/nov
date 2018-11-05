package com.minhao.nov;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.minhao.nov.dao")
public class NovApplication {

    public static void main(String[] args) {
        SpringApplication.run(NovApplication.class, args);
    }
}
