package com.finserv.controller;

import com.finserv.configuration.CustomUserDetails;
import com.finserv.dto.*;
import com.finserv.service.LoanApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/loan-applications")
@RequiredArgsConstructor
public class LoanApplicationController {

    private final LoanApplicationService loanApplicationService;

    @PostMapping("/apply-by-dealer/{dealerId}")
    public ResponseEntity<?> applyByDealer(
            @PathVariable Long dealerId,
            @RequestBody DealerLoanApplicationRequestDTO dto
    ) {

        if (dealerId == null) {
            return ResponseEntity.badRequest().body("Dealer id is required");
        }

        if (dto.getFullName() == null || dto.getFullName().isBlank()) {
            return ResponseEntity.badRequest().body("Full name is required");
        }

        if (dto.getMobileNumber() == null || dto.getMobileNumber().isBlank()) {
            return ResponseEntity.badRequest().body("Mobile number is required");
        }

        if (!dto.getMobileNumber().matches("^[6-9][0-9]{9}$")) {
            return ResponseEntity.badRequest().body("Enter valid mobile number");
        }

        if (dto.getLoanAmount() == null || dto.getLoanAmount() <= 0) {
            return ResponseEntity.badRequest().body("Loan amount is required");
        }

        return ResponseEntity.ok(
                loanApplicationService.applyByDealer(
                        dealerId,
                        dto
                )
        );
    }

    @PostMapping("/personal/{applicationNumber}")
    public ResponseEntity<?> savePersonalInfo(
            @PathVariable String applicationNumber,
            @RequestBody LoanPersonalInfoDTO dto
    ) {

        if (applicationNumber == null || applicationNumber.isBlank()) {
            return ResponseEntity.badRequest().body("Application number is required");
        }

        if (dto.getFullName() == null || dto.getFullName().isBlank()) {
            return ResponseEntity.badRequest().body("Full name is required");
        }

        if (dto.getMobileNumber() == null || dto.getMobileNumber().isBlank()) {
            return ResponseEntity.badRequest().body("Mobile number is required");
        }

        if (!dto.getMobileNumber().matches("^[6-9][0-9]{9}$")) {
            return ResponseEntity.badRequest().body("Enter valid mobile number");
        }

        if (dto.getEmail() == null || dto.getEmail().isBlank()) {
            return ResponseEntity.badRequest().body("Email is required");
        }

        if (!dto.getEmail().matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return ResponseEntity.badRequest().body("Enter valid email");
        }

        if (dto.getAddress() == null || dto.getAddress().isBlank()) {
            return ResponseEntity.badRequest().body("Address is required");
        }

        if (dto.getCity() == null || dto.getCity().isBlank()) {
            return ResponseEntity.badRequest().body("City is required");
        }

        if (dto.getState() == null || dto.getState().isBlank()) {
            return ResponseEntity.badRequest().body("State is required");
        }

        if (dto.getPincode() == null || dto.getPincode().isBlank()) {
            return ResponseEntity.badRequest().body("Pincode is required");
        }

        if (!dto.getPincode().matches("^[0-9]{6}$")) {
            return ResponseEntity.badRequest().body("Enter valid pincode");
        }

        return ResponseEntity.ok(
                loanApplicationService.savePersonalInfo(applicationNumber, dto)
        );
    }

    @PostMapping("/kyc/{applicationNumber}")
    public ResponseEntity<?> saveKyc(
            @PathVariable String applicationNumber,
            @RequestBody LoanKycDTO dto
    ) {

        if (dto.getPanDocumentId() == null) {
            return ResponseEntity.badRequest().body("PAN document is required");
        }

        if (dto.getAadhaarFrontDocumentId() == null) {
            return ResponseEntity.badRequest().body("Aadhaar front document is required");
        }

        if (dto.getAadhaarBackDocumentId() == null) {
            return ResponseEntity.badRequest().body("Aadhaar back document is required");
        }

        return ResponseEntity.ok(
                loanApplicationService.saveKyc(applicationNumber, dto)
        );
    }

    @PostMapping("/residential/{applicationNumber}")
    public ResponseEntity<?> saveResidential(
            @PathVariable String applicationNumber,
            @RequestBody LoanResidentialDTO dto
    ) {

        if (dto.getResidentialProofDocumentId() == null) {
            return ResponseEntity.badRequest().body("Residential proof document is required");
        }

        return ResponseEntity.ok(
                loanApplicationService.saveResidential(applicationNumber, dto)
        );
    }

    @PostMapping("/income/{applicationNumber}")
    public ResponseEntity<?> saveIncome(
            @PathVariable String applicationNumber,
            @RequestBody LoanIncomeDTO dto
    ) {

        if (dto.getEmploymentType() == null) {
            return ResponseEntity.badRequest().body("Employment type is required");
        }

        if (dto.getBankStatementDocumentId() == null) {
            return ResponseEntity.badRequest().body("Bank statement document is required");
        }

        return ResponseEntity.ok(
                loanApplicationService.saveIncome(applicationNumber, dto)
        );
    }

    @PostMapping("/vehicle/{applicationNumber}")
    public ResponseEntity<?> saveVehicle(
            @PathVariable String applicationNumber,
            @RequestBody LoanVehicleDTO dto
    ) {

        if (dto.getRcDocumentId() == null) {
            return ResponseEntity.badRequest().body("RC document is required");
        }

        if (dto.getInsuranceDocumentId() == null) {
            return ResponseEntity.badRequest().body("Insurance document is required");
        }

        if (dto.getOdometerReading() == null || dto.getOdometerReading().isBlank()) {
            return ResponseEntity.badRequest().body("Odometer reading is required");
        }

        if (dto.getChassisNumber() == null || dto.getChassisNumber().isBlank()) {
            return ResponseEntity.badRequest().body("Chassis number is required");
        }

        return ResponseEntity.ok(
                loanApplicationService.saveVehicle(applicationNumber, dto)
        );
    }

    @PostMapping("/submit/{applicationNumber}")
    public ResponseEntity<?> submitApplication(
            @PathVariable String applicationNumber
    ) {
        return ResponseEntity.ok(
                loanApplicationService.submitApplication(applicationNumber)
        );
    }

    @GetMapping("/{applicationNumber}")
    public ResponseEntity<?> getByApplicationNumber(
            @PathVariable String applicationNumber
    ) {
        return ResponseEntity.ok(
                loanApplicationService.getByApplicationNumber(applicationNumber)
        );
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserApplications(
            @PathVariable Long userId
    ) {
        return ResponseEntity.ok(
                loanApplicationService.getByUserId(userId)
        );
    }

//    @GetMapping("/dealer/{dealerId}")
//    public ResponseEntity<?> getDealerApplications(
//            @PathVariable Long dealerId
//    ) {
//        return ResponseEntity.ok(
//                loanApplicationService.getByDealerId(dealerId)
//        );
//    }

    @GetMapping("/dealer/{dealerId}")
    public ResponseEntity<?> getDealerApplications(
            @PathVariable Long dealerId,
            Authentication authentication
    ) {
        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();

        if (!user.getId().equals(dealerId)) {
            return ResponseEntity.status(403).body("Unauthorized dealer");
        }

        return ResponseEntity.ok(
                loanApplicationService.getByDealerId(dealerId)
        );
    }

    @GetMapping("/admin/all")
    public ResponseEntity<?> getAllApplications() {
        return ResponseEntity.ok(
                loanApplicationService.getAllApplications()
        );
    }


    @PutMapping("/personal/{applicationNumber}")
    public ResponseEntity<?> updatePersonalInfo(
            @PathVariable String applicationNumber,
            @RequestBody LoanPersonalInfoDTO dto
    ) {
        return ResponseEntity.ok(
                loanApplicationService.savePersonalInfo(
                        applicationNumber,
                        dto
                )
        );
    }


    @PutMapping("/kyc/{applicationNumber}")
    public ResponseEntity<?> updateKyc(
            @PathVariable String applicationNumber,
            @RequestBody LoanKycDTO dto
    ) {
        return ResponseEntity.ok(
                loanApplicationService.saveKyc(
                        applicationNumber,
                        dto
                )
        );
    }

    @PutMapping("/residential/{applicationNumber}")
    public ResponseEntity<?> updateResidential(
            @PathVariable String applicationNumber,
            @RequestBody LoanResidentialDTO dto
    ) {
        return ResponseEntity.ok(
                loanApplicationService.saveResidential(
                        applicationNumber,
                        dto
                )
        );
    }


    @PutMapping("/income/{applicationNumber}")
    public ResponseEntity<?> updateIncome(
            @PathVariable String applicationNumber,
            @RequestBody LoanIncomeDTO dto
    ) {
        return ResponseEntity.ok(
                loanApplicationService.saveIncome(
                        applicationNumber,
                        dto
                )
        );
    }

    @PutMapping("/vehicle/{applicationNumber}")
    public ResponseEntity<?> updateVehicle(
            @PathVariable String applicationNumber,
            @RequestBody LoanVehicleDTO dto
    ) {
        return ResponseEntity.ok(
                loanApplicationService.saveVehicle(
                        applicationNumber,
                        dto
                )
        );
    }
}