package com.finserv.repository;

import com.finserv.entity.UserKyc;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserKycRepository extends JpaRepository<UserKyc, Long> {

    Optional<UserKyc> findByUserUserId(Long userId);
}
