package com.yggdrasil.servicefrommysql.service.api;


import com.yggdrasil.servicefrommysql.dto.UserMysqlDTO;

import java.util.List;

public interface UserMysqlService {

    UserMysqlDTO create(UserMysqlDTO dto);

    UserMysqlDTO update(Long id, UserMysqlDTO dto);

    UserMysqlDTO getById(Long id);

    List<UserMysqlDTO> getAll();

    void delete(Long id);

    List<UserMysqlDTO> find();
}