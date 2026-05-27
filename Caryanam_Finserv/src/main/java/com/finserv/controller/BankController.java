package com.finserv.controller;

import com.finserv.dto.BankRequestDTO;
import com.finserv.dto.BankResponseDTO;
import com.finserv.dto.ResponseDto;
import com.finserv.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bank")
@RequiredArgsConstructor
public class BankController {

    private final BankService bankService;

    // SAVE BANK
    @PostMapping("/add")
    public ResponseEntity<ResponseDto<BankResponseDTO>> saveBank(
            @RequestBody BankRequestDTO dto) {

        // NULL CHECK
        if (dto == null) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Request Body is Missing",
                            null
                    ));
        }

        // ADMIN ID VALIDATION
        if (dto.getAdminId() == null) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Admin Id is Required",
                            null
                    ));
        }

        // ADMIN ID LIMIT
        if (dto.getAdminId() <= 0) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Valid Admin Id is Required",
                            null
                    ));
        }

        // BANK NAME VALIDATION
        if (dto.getBankName() == null
                || dto.getBankName().trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Bank Name is Required",
                            null
                    ));
        }

        // BANK NAME CHARACTER LIMIT
        if (dto.getBankName().length() > 30) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Bank Name must be maximum 30 characters",
                            null
                    ));
        }

        // BANK NAME FORMAT VALIDATION
        if (!dto.getBankName()
                .matches("^[A-Za-z ]+$")) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Bank Name must contain only letters and spaces",
                            null
                    ));
        }
        if (dto.getWhatsappNumber() == null || dto.getWhatsappNumber().trim().isEmpty()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(400, "WhatsApp Number is Required", null));
        }

        if (!dto.getWhatsappNumber().matches("^[6-9]\\d{9}$")) {
            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(400, "Invalid WhatsApp Number", null));
        }

        // ROI MIN VALIDATION
        if (dto.getRoiMin() == null) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "ROI Min is Required",
                            null
                    ));
        }

        if (dto.getRoiMin() <= 0) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "ROI Min must be greater than 0",
                            null
                    ));
        }

        // ROI MIN LIMIT
        if (dto.getRoiMin() > 100) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "ROI Min cannot be greater than 100",
                            null
                    ));
        }

        // ROI MAX VALIDATION
        if (dto.getRoiMax() == null) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "ROI Max is Required",
                            null
                    ));
        }

        if (dto.getRoiMax() <= 0) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "ROI Max must be greater than 0",
                            null
                    ));
        }

        // ROI MAX LIMIT
        if (dto.getRoiMax() > 100) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "ROI Max cannot be greater than 100",
                            null
                    ));
        }

        // ROI COMPARISON
        if (dto.getRoiMin() > dto.getRoiMax()) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "ROI Min cannot be greater than ROI Max",
                            null
                    ));
        }

        // PROCESSING DAYS VALIDATION
        if (dto.getProcessingDays() == null
                || dto.getProcessingDays().trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Processing Days is Required",
                            null
                    ));
        }

        // PROCESSING DAYS CHARACTER LIMIT
        if (dto.getProcessingDays().length() > 20) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Processing Days must be maximum 20 characters",
                            null
                    ));
        }

        // MAX LTV VALIDATION
        if (dto.getMaxLtv() == null) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Max LTV is Required",
                            null
                    ));
        }

        if (dto.getMaxLtv() <= 0
                || dto.getMaxLtv() > 100) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Max LTV must be between 1 to 100",
                            null
                    ));
        }

        // MAX TENURE VALIDATION
        if (dto.getMaxTenureMonths() == null) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Max Tenure Months is Required",
                            null
                    ));
        }

        if (dto.getMaxTenureMonths() <= 0) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Max Tenure Months must be greater than 0",
                            null
                    ));
        }

        // MAX TENURE LIMIT
        if (dto.getMaxTenureMonths() > 600) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Max Tenure Months must be less than 600",
                            null
                    ));
        }

        // FEATURES VALIDATION
        if (dto.getFeatures() == null
                || dto.getFeatures().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "At least one Feature is Required",
                            null
                    ));
        }

        // FEATURES SIZE LIMIT
        if (dto.getFeatures().size() > 20) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Maximum 20 Features are allowed",
                            null
                    ));
        }

        // CASES THIS MONTH VALIDATION
        if (dto.getCasesThisMonth() == null) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Cases This Month is Required",
                            null
                    ));
        }

        if (dto.getCasesThisMonth() < 0) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Cases This Month cannot be negative",
                            null
                    ));
        }

        // CASES LIMIT
        if (dto.getCasesThisMonth() > 100000) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Cases This Month is too large",
                            null
                    ));
        }

        // STATUS VALIDATION
        if (dto.getStatus() == null
                || dto.getStatus().trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Status is Required",
                            null
                    ));
        }

        // STATUS CHARACTER LIMIT
        if (dto.getStatus().length() > 20) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Status must be maximum 20 characters",
                            null
                    ));
        }

        if (!dto.getStatus().equalsIgnoreCase("ACTIVE")
                && !dto.getStatus().equalsIgnoreCase("INACTIVE")) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Status must be ACTIVE or INACTIVE",
                            null
                    ));
        }

        // SAVE BANK
        BankResponseDTO response =
                bankService.saveBank(dto);

        return ResponseEntity.status(201)
                .body(new ResponseDto<>(
                        201,
                        "Bank Added Successfully",
                        response
                ));
    }

    // GET ALL BANKS
    @GetMapping("/all")
    public ResponseEntity<ResponseDto<List<BankResponseDTO>>> getAllBanks() {

        return ResponseEntity.ok(
                new ResponseDto<>(
                        200,
                        "All Banks",
                        bankService.getAllBanks()
                )
        );
    }

    // GET BANK BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto<BankResponseDTO>> getBankById(
            @PathVariable Long id) {

        // ID VALIDATION
        if (id == null || id <= 0) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Valid Bank Id is Required",
                            null
                    ));
        }

        return ResponseEntity.ok(
                new ResponseDto<>(
                        200,
                        "Bank Found",
                        bankService.getBankById(id)
                )
        );
    }

    // UPDATE BANK
    @PutMapping("/update/{id}")
    public ResponseEntity<ResponseDto<BankResponseDTO>> updateBank(
            @PathVariable Long id,
            @RequestBody BankRequestDTO dto) {

        // ID VALIDATION
        if (id == null || id <= 0) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Valid Bank Id is Required",
                            null
                    ));
        }

        // NULL CHECK
        if (dto == null) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Request Body is Missing",
                            null
                    ));
        }

        BankResponseDTO response =
                bankService.updateBank(id, dto);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        200,
                        "Bank Updated Successfully",
                        response
                )
        );
    }

    // DELETE BANK
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ResponseDto<String>> deleteBank(
            @PathVariable Long id) {

        // ID VALIDATION
        if (id == null || id <= 0) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Valid Bank Id is Required",
                            null
                    ));
        }

        bankService.deleteBank(id);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        200,
                        "Bank Deleted Successfully",
                        "Deleted"
                )
        );
    }

    // UPDATE STATUS
    @PatchMapping("/status/{id}")
    public ResponseEntity<ResponseDto<BankResponseDTO>>
    updateStatus(
            @PathVariable Long id,
            @RequestParam String status) {

        // ID VALIDATION
        if (id == null || id <= 0) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Valid Bank Id is Required",
                            null
                    ));
        }

        // STATUS VALIDATION
        if (status == null
                || status.trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Status is Required",
                            null
                    ));
        }

        // STATUS CHARACTER LIMIT
        if (status.length() > 20) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Status must be maximum 20 characters",
                            null
                    ));
        }

        if (!status.equalsIgnoreCase("ACTIVE")
                && !status.equalsIgnoreCase("INACTIVE")) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Status must be ACTIVE or INACTIVE",
                            null
                    ));
        }

        BankResponseDTO response =
                bankService.updateStatus(id, status);

        return ResponseEntity.ok(
                new ResponseDto<>(
                        200,
                        "Status Updated Successfully",
                        response
                )
        );
    }
}