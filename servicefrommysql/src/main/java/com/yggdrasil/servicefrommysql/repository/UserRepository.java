package com.yggdrasil.servicefrommysql.repository;

import com.yggdrasil.servicefrommysql.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
