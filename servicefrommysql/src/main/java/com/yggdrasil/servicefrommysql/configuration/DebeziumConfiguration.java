package com.yggdrasil.servicefrommysql.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.io.IOException;

@Configuration
public class DebeziumConfiguration {

    @Bean
    public io.debezium.config.Configuration mysqlConnector() throws IOException {

        // Create storage file for storing offset from Debezium
        File offsetStorageTempFile = File.createTempFile("offsets_", ".dat");

        return io.debezium.config.Configuration.create()
                .with("name", "mysql-connector")  // Create a name for connector
                .with("connector.class", "io.debezium.connector.mysql.MySqlConnector")  // Use MySQL connector
                .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
                .with("offset.storage.file.filename", offsetStorageTempFile.getAbsolutePath())
                .with("offset.flush.interval.ms", "60000")  // Interval for storing offset
                .with("max.queue.size", "8192")
                .with("max.batch.size", "2048")
                .with("database.hostname", "mysql")
                .with("database.port", "3306")  // Port MySQL
                .with("database.user", "username")
                .with("database.password", "password")
                .with("database.dbname", "testmysqlDB")
                .with("table.whitelist", "testmysqlDB.user_mysql")
                .with("plugin.name", "mysql")
                .with("include.schema.changes", "false")  // not include change from schema
                .with("database.server.id", "184054")  // Unique server ID
                .with("topic.prefix", "dbserver1")
                .with("database.include.list", "inventory")
                .with("schema.history.internal.kafka.bootstrap.servers", "kafka:9092")
                .with("schema.history.internal.kafka.topic", "schema-changes.inventory")
                .build();
    }
}
