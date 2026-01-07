package com.yggdrasil.servicefrommysql.listener;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DebeziumConnectorProvisioner {

    private final WebClient connect;

    @PostConstruct
    public void provision() {
        waitUntilConnectReady();

        String name = "inventory-connector";

        Map<String, Object> config = new HashMap<>();

        config.put("connector.class", "io.debezium.connector.mysql.MySqlConnector");
        config.put("tasks.max", "1");
        config.put("database.hostname", "mysql");
        config.put("database.port", "3306");
        config.put("database.user", "debezium");
        config.put("database.password", "dbz");
        config.put("database.server.id", "184054");
        config.put("topic.prefix", "dbserver1");
        config.put("database.include.list", "inventory");
        config.put("schema.history.internal.kafka.bootstrap.servers", "kafka:9092");
        config.put("schema.history.internal.kafka.topic", "schemahistory.inventory");

        // idempotent: if exists -> PUT config; else -> POST create
        if (connectorExists(name)) {
            log.info("Connector {} exists -> updating config", name);
            putConfig(name, config);
        } else {
            log.info("Connector {} not found -> creating", name);
            createConnector(name, config);
        }

        log.info("Provision done. Status: {}", getStatus(name));
    }

    private boolean connectorExists(String name) {
        try {
            connect.get().uri("/connectors/{name}", name)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            return true;
        } catch (WebClientResponseException.NotFound e) {
            return false;
        }
    }


    private void putConfig(String name, Map<String, Object> config) {
        connect.put().uri("/connectors/{name}/config", name)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(config)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


    private void createConnector(String name, Map<String, Object> config) {
        Map<String, Object> payload = Map.of("name", name, "config", config);

        connect.post().uri("/connectors")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


    private String getStatus(String name) {
        return connect.get().uri("/connectors/{name}/status", name)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private void waitUntilConnectReady() {
        for (int i = 1; i <= 60; i++) {
            try {
                connect.get().uri("/connectors")
                        .retrieve()
                        .bodyToMono(String.class)
                        .block();
                log.info("Kafka Connect is ready");
                return;
            } catch (Exception e) {
                log.info("Waiting for Kafka Connect... {}/60 ({})", i, e.getMessage());
                sleep(1000);
            }
        }
        throw new IllegalStateException("Kafka Connect not ready after 60s");
    }

    private void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
