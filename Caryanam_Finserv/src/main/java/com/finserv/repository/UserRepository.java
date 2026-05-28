package com.finserv.repository;
import com.finserv.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByMobileNumber(String mobileNumber);


    Optional<User> findTopByOrderByUserIdDesc();


    @Query("SELECT u FROM User u LEFT JOIN FETCH u.personalInfo")
    List<User> findAllUsersWithPersonalInfo();
}