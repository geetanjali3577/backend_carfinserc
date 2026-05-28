package com.finserv.service;

import com.finserv.dto.PersonalInfoRequestDTO;

public interface PersonalInfoService {

    Object savePersonalInfo(
            PersonalInfoRequestDTO dto
    );
}