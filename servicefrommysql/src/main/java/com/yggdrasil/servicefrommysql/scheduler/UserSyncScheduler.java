package com.yggdrasil.servicefrommysql.scheduler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yggdrasil.servicefrommysql.dto.UserMysqlDTO;
import com.yggdrasil.servicefrommysql.service.api.UserMysqlService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class UserSyncScheduler {

    private final KafkaTemplate<String, String> kafkaTemplate;

    private final UserMysqlService userMysqlService;

    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void process() throws JsonProcessingException {
        log.info("Processing ==========> Started");
        List<UserMysqlDTO> userDTOS = userMysqlService.find();

        for (UserMysqlDTO userDTO : userDTOS) {
            kafkaTemplate.send("user-topic", objectMapper.writeValueAsString(userDTO));
            userMysqlService.updateStatus(userDTO.id(), 1);
        }
    }
}
