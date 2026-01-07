package com.yggdrasil.servicefromhtwo.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yggdrasil.servicefromhtwo.dto.UserHtwoDTO;
import com.yggdrasil.servicefromhtwo.entity.UserHtwo;
import com.yggdrasil.servicefromhtwo.repository.UserHtwoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class Consumer {

    private final UserHtwoRepository userHtwoRepository;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "dbserver1.inventory.user_mysql", groupId = "user-group")
    public void consumeMessage(String message) throws JsonProcessingException {
        log.info("Consume message: " + message);

//        UserHtwoDTO userDTO = objectMapper.readValue(message, UserHtwoDTO.class);
//
//        UserHtwo userHtwo = UserHtwo.builder()
//                .name(userDTO.name())
//                .email(userDTO.email())
//                .build();
//
//        userHtwoRepository.save(userHtwo);
    }
}
