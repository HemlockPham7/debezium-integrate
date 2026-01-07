//package com.yggdrasil.servicefrommysql.listener;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.yggdrasil.servicefrommysql.entity.UserMysql;
//import io.debezium.config.Configuration;
//import io.debezium.embedded.Connect;
//import io.debezium.engine.DebeziumEngine;
//import io.debezium.engine.RecordChangeEvent;
//import io.debezium.engine.format.ChangeEventFormat;
//import jakarta.annotation.PostConstruct;
//import jakarta.annotation.PreDestroy;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.kafka.connect.data.Field;
//import org.apache.kafka.connect.data.Struct;
//import org.apache.kafka.connect.source.SourceRecord;
//import org.json.JSONObject;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.Executor;
//import java.util.concurrent.Executors;
//
//@Slf4j
//@Component
//public class DebeziumEventListener {
//
//    private final Executor executor;
//
//    private final DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;
//
//    private final ObjectMapper objectMapper;
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    public DebeziumEventListener(Configuration mysqlConnector, ObjectMapper objectMapper, KafkaTemplate<String, String> kafkaTemplate) {
//        this.objectMapper = objectMapper;
//        this.kafkaTemplate = kafkaTemplate;
//        this.executor = Executors.newSingleThreadExecutor();
//
//        this.debeziumEngine = DebeziumEngine.create(ChangeEventFormat.of(Connect.class))
//                        .using(mysqlConnector.asProperties())
//                        .notifying(this::handleChangeEvent)
//                        .build();
//    }
//
//    private void handleChangeEvent(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {
//
//        final SourceRecord sourceRecord = sourceRecordRecordChangeEvent.record();
//
//        final Struct sourceRecordKey = (Struct) sourceRecord.key();
//
//        final Struct sourceRecordValue = (Struct) sourceRecord.value();
//
//        log.info("SourceRecord: {}", sourceRecord);
//        log.info("SourceRecordKey: {}", sourceRecordKey);
//        log.info("SourceRecordValue: {}", sourceRecordValue);
//
//        final String op = sourceRecordValue.schema().fields().stream()
//                .anyMatch(f -> f.name().equals("op")) ? (String) sourceRecordValue.get("op") : null;
//
//        if (op == null) {
//            log.info("This is not a row change event (maybe DDL or snapshot metadata), skipping...");
//            return;
//        }
//
//        // Handle 'after' field only if it's an actual row change event
//        Struct afterStruct = (Struct) sourceRecordValue.get("after");
//        if (afterStruct == null) {
//            log.info("After struct is null (maybe a delete event), skipping...");
//            return;
//        }
//
//        log.info("afterStruct: {}", afterStruct);
//
//        // Convert the 'after' struct to a map (key-value pairs)
//        Map<String, Object> afterMap = new HashMap<>();
//        for (Field field : afterStruct.schema().fields()) {
//            afterMap.put(field.name(), afterStruct.get(field));
//        }
//
//        // Convert map to JSON string for further processing
//        String json;
//        try {
//            json = new ObjectMapper().writeValueAsString(afterMap);
//        } catch (JsonProcessingException e) {
//            log.error("Error while converting 'after' struct to JSON", e);
//            return;
//        }
//
//        log.info("After JSON: {}", json);
//
//        UserMysql userOutbox = null;
//        try {
//            userOutbox = objectMapper.readValue(json, UserMysql.class);
//            kafkaTemplate.send("user-topic", objectMapper.writeValueAsString(userOutbox));
//        } catch (JsonProcessingException e) {
//            log.error("Error while deserializing JSON to UserMysql object", e);
//            throw new RuntimeException(e);
//        }
//
//        log.info("User Outbox Log: {}", userOutbox);
//    }
//
//
//    @PostConstruct
//    private void start() {
//        log.info("==============> Starting");
//        this.executor.execute(debeziumEngine);
//    }
//
//    @PreDestroy
//    private void stop() throws IOException {
//        log.info("==============> Stop");
//        if (this.debeziumEngine != null) {
//            this.debeziumEngine.close();
//        }
//    }
//
//}