package com.yggdrasil.servicefrommysql.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ConnectClientConfig {

    @Value("${connect.url}")
    private String connectUrl;

    @Bean
    WebClient connectWebClient() {
        return WebClient.builder()
                .baseUrl(connectUrl)
                .build();
    }
}
