package com.bouquet.api.user.repository;


import com.bouquet.api.user.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //Optional<User> findByEmail(String email);

    @Query(value = "SELECT * FROM user WHERE social_login_type=1 AND social_id=:socialId", nativeQuery = true)
    User findByKakaoSocialId(@Param("socialId") String socialId);

    @Query(value = "SELECT * FROM user WHERE social_login_type=0 AND social_id=:socialId", nativeQuery = true)
    User findByGoogleSocialId(@Param("socialId") String socialId);

}
