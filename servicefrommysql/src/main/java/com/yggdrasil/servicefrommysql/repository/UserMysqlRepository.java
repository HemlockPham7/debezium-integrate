package com.yggdrasil.servicefrommysql.repository;

import com.yggdrasil.servicefrommysql.entity.UserMysql;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserMysqlRepository extends JpaRepository<UserMysql, Long> {

    List<UserMysql> findByStatus(Integer status);
}
