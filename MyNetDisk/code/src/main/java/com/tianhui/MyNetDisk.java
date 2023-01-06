package com.tianhui;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.tianhui.Dao")
public class MyNetDisk {
    public static void main(String[] args) {
        SpringApplication.run(MyNetDisk.class, args);
    }
}
