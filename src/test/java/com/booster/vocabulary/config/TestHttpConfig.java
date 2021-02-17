package com.booster.vocabulary.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;

@Profile("test")
@Configuration
public class TestHttpConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
