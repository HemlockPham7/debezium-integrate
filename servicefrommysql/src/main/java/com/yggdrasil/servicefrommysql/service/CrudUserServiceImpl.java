package com.yggdrasil.servicefrommysql.service;

import com.yggdrasil.servicefrommysql.dto.UserDTO;
import com.yggdrasil.servicefrommysql.entity.User;
import com.yggdrasil.servicefrommysql.repository.UserRepository;
import com.yggdrasil.servicefrommysql.service.api.CrudUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CrudUserServiceImpl implements CrudUserService {

    private final UserRepository userRepository;

    @Override
    public UserDTO create(UserDTO dto) {
        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .build();

        return toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO update(Long id, UserDTO dto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setName(dto.name());
        user.setEmail(dto.email());

        return toDTO(userRepository.save(user));
    }

    @Override
    public UserDTO getById(Long id) {
        return userRepository.findById(id)
                .map(this::toDTO)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public List<UserDTO> getAll() {
        return userRepository.findAll()
                .stream()
                .map(this::toDTO)
                .toList();
    }

    @Override
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
