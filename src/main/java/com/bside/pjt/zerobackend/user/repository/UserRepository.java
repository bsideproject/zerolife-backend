package com.bside.pjt.zerobackend.user.repository;

import com.bside.pjt.zerobackend.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByIdAndDeletedFalse(final long userId);
}
