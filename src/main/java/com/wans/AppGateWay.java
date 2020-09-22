package com.wans;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * Created by admin on 2020/9/17.
 */
@SpringBootApplication
@MapperScan("com.wans.mapper")
public class AppGateWay {
    public static void main(String[] args) {
        SpringApplication.run(AppGateWay.class);
    }
}
