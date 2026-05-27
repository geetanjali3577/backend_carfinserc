package com.finserv.entity;

import com.finserv.enums.BankFeature;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "banks")
@Data
public class Bank {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String bankName;

    private String whatsappNumber;

    private double roiMin;

    private double roiMax;

    private String processingDays;

    private int maxLtv;

    private int maxTenureMonths;

    @ElementCollection(targetClass = BankFeature.class)
    @Enumerated(EnumType.STRING)
    @Column(name = "feature")
    private List<BankFeature> features;

    private int casesThisMonth;

    private String status;

    @PrePersist
    public void setDefaultStatus() {

        if (this.status == null || this.status.isEmpty()) {
            this.status = "ACTIVE";
        }}
    private Long adminId;
}