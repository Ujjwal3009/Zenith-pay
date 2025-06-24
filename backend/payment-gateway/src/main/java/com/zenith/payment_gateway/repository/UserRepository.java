package com.zenith.payment_gateway.repository;

import com.zenith.payment_gateway.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository  extends JpaRepository<UserEntity , Long> {
    boolean existsByEmail(String email);
    Optional<UserEntity> findByEmail(String email);
}
