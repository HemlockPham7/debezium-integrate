package com.yggdrasil.servicefromhtwo.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaHtwoTopicConfig {

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("user-topic")
                .partitions(2)
                .build();
    }
}