package com.vmark.backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.vmark.backend.mapper")
@SpringBootApplication
public class VMarkBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(VMarkBackendApplication.class, args);
    }

}
