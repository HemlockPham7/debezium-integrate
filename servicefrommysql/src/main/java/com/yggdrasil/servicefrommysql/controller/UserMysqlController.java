package com.yggdrasil.servicefrommysql.controller;

import com.yggdrasil.servicefrommysql.dto.UserMysqlDTO;
import com.yggdrasil.servicefrommysql.service.api.UserMysqlService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;

import java.util.List;

@RestController
@RequestMapping("/api/usersql")
@RequiredArgsConstructor
public class UserMysqlController {

    private final UserMysqlService userMysqlService;

    @PostMapping
    public UserMysqlDTO create(@RequestBody UserMysqlDTO dto) {
        return userMysqlService.create(dto);
    }

    @PutMapping("/{id}")
    public UserMysqlDTO update(@PathVariable Long id,
                               @RequestBody UserMysqlDTO dto) {
        return userMysqlService.update(id, dto);
    }

    @GetMapping("/{id}")
    public UserMysqlDTO getById(@PathVariable Long id) {
        return userMysqlService.getById(id);
    }

    @GetMapping
    public List<UserMysqlDTO> getAll() {
        return userMysqlService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        userMysqlService.delete(id);
    }

    @PostMapping("/synch/{id}")
    public void synchronousUserDatabase(@PathVariable Long id) {
        userMysqlService.synchUserById(id);
    }
}
