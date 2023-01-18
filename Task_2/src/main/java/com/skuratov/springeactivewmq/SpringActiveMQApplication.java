package com.skuratov.springeactivewmq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class SpringActiveMQApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringActiveMQApplication.class, args);
    }
}
