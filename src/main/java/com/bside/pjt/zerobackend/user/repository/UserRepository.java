package com.bside.pjt.zerobackend.user.repository;

import com.bside.pjt.zerobackend.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
