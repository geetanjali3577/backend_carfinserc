package com.finserv.controller;

import com.finserv.dto.ResponseDto;
import com.finserv.dto.UserRegisterDTO;
import com.finserv.dto.UserResponseDTO;
import com.finserv.entity.Dealer;
import com.finserv.enums.DealerStatus;

import com.finserv.enums.RegistrationType;
import com.finserv.repository.DealerRepository;
import com.finserv.repository.UserRepository;
import com.finserv.service.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final UserRepository userRepository;

    private final DealerRepository dealerRepository;

    @PostMapping("/register")
    public ResponseEntity<ResponseDto<UserResponseDTO>> registerUser(@RequestBody UserRegisterDTO dto) {

        // NULL CHECK
        if (dto == null) {
            return ResponseEntity.badRequest().body(new ResponseDto<>(400, "Request Body is Missing", null));
        }

        // FULL NAME VALIDATION
        if (dto.getFullName() == null || dto.getFullName().trim().isEmpty()) {

            return ResponseEntity.badRequest().body(new ResponseDto<>(400, "Full Name is Required", null));
        }

        // FULL NAME CHARACTER LIMIT
        if (dto.getFullName().length() > 30) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Full Name must be maximum 30 characters",
                            null
                    ));
        }

        if (!dto.getFullName().matches("^[A-Za-z ]+$")) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Full Name must contain only letters and spaces",
                            null
                    ));
        }

        // EMAIL VALIDATION
        if (dto.getEmail() == null
                || dto.getEmail().trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Email is Required",
                            null
                    ));
        }

        // EMAIL CHARACTER LIMIT
        if (dto.getEmail().length() > 20) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Email must be maximum 20 characters",
                            null
                    ));
        }

        if (!dto.getEmail()
                .matches("^[A-Za-z0-9._%+-]+@gmail\\.com$")) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Only Gmail format allowed",
                            null
                    ));
        }

        dto.setEmail(dto.getEmail().toLowerCase().trim());

        // DUPLICATE EMAIL CHECK
        if (userRepository.existsByEmail(dto.getEmail())) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Email Already Exists",
                            null
                    ));
        }

        // MOBILE VALIDATION
        if (dto.getMobileNumber() == null
                || dto.getMobileNumber().trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Mobile Number is Required",
                            null
                    ));
        }

        // MOBILE CHARACTER LIMIT
        if (dto.getMobileNumber().length() > 10) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Mobile Number must be maximum 10 digits",
                            null
                    ));
        }

        if (!dto.getMobileNumber().matches("\\d{10}")) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Mobile Number must be 10 digits",
                            null
                    ));
        }

        if (!dto.getMobileNumber().matches("^[6-9]\\d{9}$")) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Mobile Number must start with 6,7,8 or 9",
                            null
                    ));
        }

        // DUPLICATE MOBILE CHECK
        if (userRepository.existsByMobileNumber(dto.getMobileNumber())) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Mobile Number Already Exists",
                            null
                    ));
        }

        // PASSWORD VALIDATION
        if (dto.getPassword() == null
                || dto.getPassword().trim().isEmpty()) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Password is Required",
                            null
                    ));
        }

        // PASSWORD CHARACTER LIMIT
        if (dto.getPassword().length() > 20) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Password must be maximum 20 characters",
                            null
                    ));
        }

        if (dto.getPassword().length() < 6) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Password must be minimum 6 characters",
                            null
                    ));
        }

        // REGISTRATION TYPE VALIDATION
        if (dto.getRegistrationType() == null) {

            return ResponseEntity.badRequest()
                    .body(new ResponseDto<>(
                            400,
                            "Registration Type is Required",
                            null
                    ));
        }

        // DEALER CODE VALIDATION
        if (dto.getRegistrationType() == RegistrationType.DEALER) {

            if (dto.getDealerCode() == null
                    || dto.getDealerCode().trim().isEmpty()) {

                return ResponseEntity.badRequest()
                        .body(new ResponseDto<>(
                                400,
                                "Dealer Code is Required",
                                null
                        ));
            }

            // DEALER CODE CHARACTER LIMIT
            if (dto.getDealerCode().length() > 20) {

                return ResponseEntity.badRequest()
                        .body(new ResponseDto<>(
                                400,
                                "Dealer Code must be maximum 20 characters",
                                null
                        ));
            }

            // CHECK DEALER CODE EXISTS
            if (!dealerRepository.existsByDealerCode(
                    dto.getDealerCode())) {

                return ResponseEntity.badRequest()
                        .body(new ResponseDto<>(
                                400,
                                "Invalid Dealer Code",
                                null
                        ));
            }

            Dealer dealer =
                    dealerRepository.findByDealerCode(dto.getDealerCode())
                            .orElse(null);

            if (dealer.getStatus() == DealerStatus.INACTIVE) {

                return ResponseEntity.badRequest()
                        .body(new ResponseDto<>(
                                400,
                                "Dealer is inactive. Please contact admin.",
                                null
                        ));
            }
        }

        // REGISTER USER
        UserResponseDTO response =
                userService.registerUser(dto);

        return ResponseEntity.status(201)
                .body(new ResponseDto<>(
                        201,
                        "User Registered Successfully",
                        response
                ));
    }
}