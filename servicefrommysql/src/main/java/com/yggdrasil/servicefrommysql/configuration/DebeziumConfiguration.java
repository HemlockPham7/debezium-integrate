package com.yggdrasil.servicefrommysql.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Slf4j
@Configuration
public class DebeziumConfiguration {

    @Bean
    public io.debezium.config.Configuration mysqlConnector() throws IOException {

        log.info("******************** Starting Configurating Debezium***********");
        // Create storage file for storing offset from Debezium
        File offsetStorageTempFile = File.createTempFile("offsets_", ".dat");

        return io.debezium.config.Configuration.create()
                .with("name", "inventory-connector")
                .with("connector.class", "io.debezium.connector.mysql.MySqlConnector")
                .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", offsetStorageTempFile.getAbsolutePath())
                .with("tasks.max", "1")
                .with("database.hostname", "localhost")
                .with("database.port", "3306")
                .with("database.user", "debezium")
                .with("database.password", "dbz")
                .with("database.server.id", "184054")
                .with("topic.prefix", "dbserver1")
                .with("database.include.list", "inventory")
                .with("schema.history.internal.kafka.bootstrap.servers", "localhost:29092")
                .with("schema.history.internal.kafka.topic", "schema-changes.inventory")
                .build();
    }
}
