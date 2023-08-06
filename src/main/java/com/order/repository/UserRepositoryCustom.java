package com.order.repository;

import com.order.entity.User;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<User> findByEmail(String email);

}
