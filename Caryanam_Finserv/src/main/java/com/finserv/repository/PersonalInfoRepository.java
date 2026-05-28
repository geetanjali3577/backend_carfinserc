package com.finserv.repository;

import com.finserv.entity.PersonalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonalInfoRepository
        extends JpaRepository<PersonalInfo, Long> {

    boolean existsByUserId(Long userId);

    Optional<PersonalInfo> findByUserId(Long userId);
}