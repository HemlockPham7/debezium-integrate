//package com.yggdrasil.servicefrommysql.scheduler;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.yggdrasil.servicefrommysql.dto.UserMysqlDTO;
//import com.yggdrasil.servicefrommysql.service.api.UserMysqlService;
//import jakarta.transaction.Transactional;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.scheduling.annotation.Scheduled;
//
//import java.util.List;
//
//@Slf4j
//public class UserSyncScheduler {
//
//    private final KafkaTemplate<String, String> kafkaTemplate;
//
//    private final UserMysqlService userMysqlService;
//
//    @Scheduled(fixedRate = 30000)
//    @Transactional
//    public void process() throws JsonProcessingException {
//        log.info("Processing ==========> Started");
//        List<UserMysqlDTO> userDTOS = UserMysqlService.find();
//
//        for (UserMysqlDTO userDTO : userDTOS) {
//            kafkaTemplate.send("user-database-synchrous", objectMapper.writeValueAsString(userDTO));
//            userDTO.setStatus(1);
//            userMysqlService.update(userDTO);
//        }
//    }
//}
