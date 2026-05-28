package com.finserv.service;

import com.finserv.dto.PersonalInfoRequestDTO;
import com.finserv.dto.PersonalInfoResponseDTO;

public interface PersonalInfoService {

    Object savePersonalInfo(
            PersonalInfoRequestDTO dto
    );

    PersonalInfoResponseDTO updatePersonalInfo(Long userId, PersonalInfoRequestDTO dto);
}