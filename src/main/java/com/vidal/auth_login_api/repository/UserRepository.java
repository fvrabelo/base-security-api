package com.vidal.auth_login_api.repository;

import com.vidal.auth_login_api.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * @author Fhellipe Vidal Rabelo
 * @since 2024-06-19
 */
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
}
