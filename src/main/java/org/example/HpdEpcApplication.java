package org.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(scanBasePackages = "org.example")
@MapperScan({"org.example.mapper","org.example.mybatismapper"})
//@EnableTransactionManagement(proxyTargetClass = true)
public class HpdEpcApplication {

    public static void main(String[] args) {
        SpringApplication.run(HpdEpcApplication.class, args);
    }

}
