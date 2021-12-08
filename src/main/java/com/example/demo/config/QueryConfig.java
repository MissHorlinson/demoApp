package com.example.demo.config;

import com.cat.client.RoundingServiceClient;
import com.cat.client.RoundingServiceClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;


@Configuration
public class QueryConfig {

    @Value("${host.add}")
    private String clientAddress;

    private RestTemplate restTemplate = new RestTemplate();

    @Bean
    RoundingServiceClient createRoundingService1client() {
        return new RoundingServiceClientImpl(restTemplate, clientAddress);
    }


    // try branch

    //2
}
