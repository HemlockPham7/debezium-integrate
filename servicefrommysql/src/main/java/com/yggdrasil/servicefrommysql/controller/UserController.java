package com.yggdrasil.servicefrommysql.controller;

import com.yggdrasil.servicefrommysql.dto.UserDTO;
import com.yggdrasil.servicefrommysql.service.api.CrudUserService;
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
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final CrudUserService crudUserService;

    @PostMapping
    public UserDTO create(@RequestBody UserDTO dto) {
        return crudUserService.create(dto);
    }

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable Long id,
                          @RequestBody UserDTO dto) {
        return crudUserService.update(id, dto);
    }

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable Long id) {
        return crudUserService.getById(id);
    }

    @GetMapping
    public List<UserDTO> getAll() {
        return crudUserService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        crudUserService.delete(id);
    }
}
