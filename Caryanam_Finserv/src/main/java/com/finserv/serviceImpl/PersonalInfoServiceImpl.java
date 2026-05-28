package com.finserv.serviceImpl;

import com.finserv.dto.PersonalInfoRequestDTO;
import com.finserv.dto.PersonalInfoResponseDTO;
import com.finserv.entity.PersonalInfo;
import com.finserv.entity.User;
import com.finserv.exception.BadRequestException;
import com.finserv.repository.PersonalInfoRepository;
import com.finserv.repository.UserRepository;
import com.finserv.service.PersonalInfoService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PersonalInfoServiceImpl
        implements PersonalInfoService {

    private final PersonalInfoRepository personalInfoRepository;

    private final UserRepository userRepository;

    @Override
    public Object savePersonalInfo(
            PersonalInfoRequestDTO dto
    ) {

        // USER VALIDATION
        User user =
                userRepository.findById(dto.getUserId())
                        .orElseThrow(() ->

                                new BadRequestException(
                                        "User Not Found"
                                ));

        // DUPLICATE CHECK
        if (personalInfoRepository
                .existsByUserId(dto.getUserId())) {

            throw new BadRequestException(
                    "Personal Info Already Exists"
            );
        }

        PersonalInfo personalInfo =
                new PersonalInfo();

        // AUTO FETCH FROM USER TABLE
        personalInfo.setUserId(
                user.getUserId()
        );

        personalInfo.setFullName(
                user.getFullName()
        );

        personalInfo.setEmail(
                user.getEmail()
        );

        personalInfo.setMobileNumber(
                user.getMobileNumber()
        );

        // REQUEST DATA
        personalInfo.setAddress(
                dto.getAddress()
        );

        personalInfo.setCity(
                dto.getCity()
        );

        personalInfo.setState(
                dto.getState()
        );

        personalInfo.setPincode(
                dto.getPincode()
        );

        personalInfo.setLoanAmount(
                dto.getLoanAmount()
        );

        personalInfo.setCreatedAt(
                LocalDateTime.now()
        );

        PersonalInfo savedInfo =
                personalInfoRepository
                        .save(personalInfo);

        // RESPONSE DTO
        PersonalInfoResponseDTO response =
                new PersonalInfoResponseDTO();

        response.setPersonalInfoId(
                savedInfo.getPersonalInfoId()
        );

        response.setUserId(
                savedInfo.getUserId()
        );

        response.setFullName(
                savedInfo.getFullName()
        );

        response.setEmail(
                savedInfo.getEmail()
        );

        response.setMobileNumber(
                savedInfo.getMobileNumber()
        );

        response.setAddress(
                savedInfo.getAddress()
        );

        response.setCity(
                savedInfo.getCity()
        );

        response.setState(
                savedInfo.getState()
        );

        response.setPincode(
                savedInfo.getPincode()
        );

        response.setLoanAmount(
                savedInfo.getLoanAmount()
        );

        response.setCreatedAt(
                savedInfo.getCreatedAt()
        );

        return response;
    }
}