package com.finserv.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "personal_info")
@Data
public class PersonalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long personalInfoId;

    private Long userId;

    private String fullName;

    private String email;

    private String mobileNumber;

    @Column(length = 500)
    private String address;

    private String city;

    private String state;

    private String pincode;

    private Double loanAmount;

    private LocalDateTime createdAt;
}