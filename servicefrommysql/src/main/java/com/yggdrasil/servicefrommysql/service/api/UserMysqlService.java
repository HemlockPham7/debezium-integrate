package com.yggdrasil.servicefrommysql.service.api;


import com.yggdrasil.servicefrommysql.dto.UserMysqlDTO;

import java.util.List;

public interface UserMysqlService {

    UserMysqlDTO create(UserMysqlDTO dto);

    UserMysqlDTO update(Long id, UserMysqlDTO dto);

    UserMysqlDTO getById(Long id);

    List<UserMysqlDTO> getAll();

    void delete(Long id);

    void updateStatus(Long id, Integer newStatus);

    List<UserMysqlDTO> find();

    void synchUserById(Long id);
}