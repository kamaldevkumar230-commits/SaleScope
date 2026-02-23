package com.salescope.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.salescope.entity.User;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmailIgnoreCase(String email);

    Optional<User> findByEmailAndPasswordAndRole(String email, String password, String role);
}