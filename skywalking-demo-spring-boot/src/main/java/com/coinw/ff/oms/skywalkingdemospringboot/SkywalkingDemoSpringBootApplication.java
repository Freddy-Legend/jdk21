package com.coinw.ff.oms.skywalkingdemospringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class SkywalkingDemoSpringBootApplication {

     static void main(String[] args) {
        SpringApplication.run(SkywalkingDemoSpringBootApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
