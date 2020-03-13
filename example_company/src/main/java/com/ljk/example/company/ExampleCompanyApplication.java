package com.ljk.example.company;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @author xkey
 */
@SpringBootApplication
@EnableEurekaClient
public class ExampleCompanyApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExampleCompanyApplication.class, args);
    }

}
