package com.bm;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "com.bm.dao")//@MapperScan和dao层添加@Mapper注解意思一样
public class BmApplication {

    public static void main(String[] args) {
        SpringApplication.run(BmApplication.class, args);
    }

}
