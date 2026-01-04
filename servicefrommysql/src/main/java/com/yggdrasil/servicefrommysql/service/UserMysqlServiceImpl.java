package com.yggdrasil.servicefrommysql.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yggdrasil.servicefrommysql.dto.UserMysqlDTO;
import com.yggdrasil.servicefrommysql.entity.UserMysql;
import com.yggdrasil.servicefrommysql.repository.UserMysqlRepository;
import com.yggdrasil.servicefrommysql.service.api.UserMysqlService;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMysqlServiceImpl implements UserMysqlService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final UserMysqlRepository userRepository;
    private final ObjectMapper objectMapper;

    @Override
    public UserMysqlDTO create(UserMysqlDTO dto) {
        UserMysql userMysql = UserMysql.builder()
                .name(dto.name())
                .email(dto.email())
                .status(0)
                .build();

        return toDTO(userRepository.save(userMysql));
    }

    @Override
    public UserMysqlDTO update(Long id, UserMysqlDTO dto) {
        UserMysql userMysql = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userMysql.setName(dto.name());
        userMysql.setEmail(dto.email());

        return toDTO(userRepository.save(userMysql));
    }

    @Override
    public UserMysqlDTO getById(Long id) {
        return userRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<UserMysqlDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<UserMysqlDTO> find() {
        return userRepository.findByStatus(0)
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public void updateStatus(Long id, Integer newStatus) {
        UserMysql user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setStatus(newStatus);

        userRepository.save(user);
    }

    @Override
    public void synchUserById(Long id) {
        UserMysql userMysql = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserMysqlDTO userMysqlDTO = toDTO(userMysql);

        try {
            String userJson = objectMapper.writeValueAsString(userMysqlDTO);
            kafkaTemplate.send("user-topic", userJson);
        } catch (Exception e) {
            throw new RuntimeException("Error serializing user data for Kafka", e);
        }

        userMysql.setStatus(1);
        userRepository.save(userMysql);
    }

    private UserMysqlDTO toDTO(UserMysql userMysql) {
        return new UserMysqlDTO(
                userMysql.getId(),
                userMysql.getName(),
                userMysql.getEmail()
        );
    }
}
