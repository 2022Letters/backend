package com.bouquet.api.user.repository;


import com.bouquet.api.user.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM User WHERE email=:email", nativeQuery = true)
    User existsByEmail(@Param("email") String email);

}
