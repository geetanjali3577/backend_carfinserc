package com.finserv.entity;

import com.finserv.enums.DocumentType;
import com.finserv.enums.EmploymentType;
import com.finserv.enums.KycStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "user_kyc")
@Data
public class UserKyc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long kycId;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Enumerated(EnumType.STRING)
    private EmploymentType employmentType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "admin_id")
    private Admin verifiedBy;

    private String assignedBank;

    @Enumerated(EnumType.STRING)
    private KycStatus finalStatus = KycStatus.PENDING;

    private Boolean kycCompleted = false;

    private Boolean disbursed = false;

    @OneToMany(mappedBy = "userKyc",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<DocumentType> documents;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }


}