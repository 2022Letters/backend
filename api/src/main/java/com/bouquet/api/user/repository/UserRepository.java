package com.bouquet.api.user.repository;

import com.bouquet.api.user.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM User WHERE email=:email", nativeQuery = true)
    User existsByEmail(@Param("email") String email);
}
