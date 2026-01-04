package com.yggdrasil.servicefrommysql.dto;

public record UserMysqlDTO(
        Long id,
        String name,
        String email
) {}