package com.yggdrasil.servicefrommysql.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yggdrasil.servicefrommysql.entity.UserMysql;
import io.debezium.config.Configuration;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.json.JSONObject;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Slf4j
@Component
public class DebeziumEventListener {

    private final Executor executor;

    private final DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;

    private final ObjectMapper objectMapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public DebeziumEventListener(Configuration mysqlConnector, ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
        this.executor = Executors.newSingleThreadExecutor();

        this.debeziumEngine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
                        .using(mysqlConnector.asProperties())
                        .notifying(this::handleChangeEvent)
                        .build();
    }

    private void handleChangeEvent(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {

        final SourceRecord sourceRecord = sourceRecordRecordChangeEvent.record();

        final Struct sourceRecordKey = (Struct) sourceRecord.key();

        final Struct sourceRecordValue = (Struct) sourceRecord.value();

        log.info("SourceRecord: {}", sourceRecord);
        log.info("SourceRecordKey: {}", sourceRecordKey);
        log.info("SourceRecordValue: {}", sourceRecordValue);

        String source = sourceRecordValue.get("after").toString();

        source = source.replaceFirst("Struct", "");
        source = source.replaceFirst("\\(", "");
        source = source.substring(0, source.length() - 1);

        log.info("Source: {}", source);

        source = handle(splitString(source));

        UserMysql userOutbox = null;
        try {
            userOutbox = objectMapper.readValue(source, UserMysql.class);
            kafkaTemplate.send("user-topic", objectMapper.writeValueAsString(userOutbox));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        log.info("User Outbox Log: {}", userOutbox);
    }

    public static String handle(List<String> components) {
        JSONObject jsonObject = new JSONObject();
        for (String component : components) {
            String[] keyValue = component.split("=");
            jsonObject.put(keyValue[0], keyValue[1]);
        }
        return jsonObject.toString();
    }

    public static List<String> splitString(String input) {
        List<String> components = new ArrayList<>();

        StringBuilder builder = new StringBuilder();
        int curlyBraceCount = 0;

        for (int i = 0; i < input.length(); i++) {
            char c = input.charAt(i);

            if (c == '{') {
                curlyBraceCount++;
            } else if (c == '}') {
                curlyBraceCount--;
            }

            if (c == ',' && curlyBraceCount == 0) {
                components.add(builder.toString().trim());
                builder.setLength(0);
            } else {
                builder.append(c);
            }
        }

        components.add(builder.toString().trim());
        return components;
    }


    @PostConstruct
    private void start() {
        log.info("Starting");
        this.executor.execute(debeziumEngine);
    }

    @PreDestroy
    private void stop() throws IOException {
        log.info("Stop");
        if (this.debeziumEngine != null) {
            this.debeziumEngine.close();
        }
    }

}