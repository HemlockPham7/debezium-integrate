package com.yggdrasil.servicefrommysql.dto;

public record UserDTO(
        Long id,
        String name,
        String email
) {}