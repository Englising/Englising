package org.englising.com.englisingbe.user.repository;


import org.englising.com.englisingbe.user.dto.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUserId(Integer userId);
    Optional<User> findByEmail(String email);
    Optional<User> findByRefreshToken(String refreshToken);

}
