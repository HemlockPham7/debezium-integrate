package com.yggdrasil.servicefrommysql.service.api;


import com.yggdrasil.servicefrommysql.dto.UserDTO;

import java.util.List;

public interface CrudUserService {

    UserDTO create(UserDTO dto);

    UserDTO update(Long id, UserDTO dto);

    UserDTO getById(Long id);

    List<UserDTO> getAll();

    void delete(Long id);
}