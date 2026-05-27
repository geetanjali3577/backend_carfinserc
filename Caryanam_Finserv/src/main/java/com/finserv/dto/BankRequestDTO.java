package com.finserv.dto;



import com.finserv.enums.BankFeature;
import lombok.Data;

import java.util.List;

@Data
public class BankRequestDTO {

    private String bankName;
    private String whatsappNumber;
    private Double roiMin;

    private Double roiMax;

    private String processingDays;

    private Integer maxLtv;

    private Integer maxTenureMonths;

    private List<BankFeature> features;

    private Integer casesThisMonth;

    private String status;

    private Long adminId;
}