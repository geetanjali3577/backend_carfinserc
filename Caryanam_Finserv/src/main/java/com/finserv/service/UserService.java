package com.finserv.service;

import com.finserv.dto.UserRegisterDTO;
import com.finserv.dto.UserResponseDTO;

public interface UserService {

    //========================================
    // REGISTER USER
    //========================================
    UserResponseDTO registerUser(
            UserRegisterDTO dto
    );

    //========================================
    // GENERATE APPLICATION ID
    //========================================
    String generateApplicationId();
}