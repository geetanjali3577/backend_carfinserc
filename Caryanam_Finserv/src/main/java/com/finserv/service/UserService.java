package com.finserv.service;

import com.finserv.dto.UserRegisterDTO;
import com.finserv.dto.UserResponseDTO;

import java.util.List;

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


    UserResponseDTO getUserById(Long id);

    List<UserResponseDTO> getAllUsers();

    UserResponseDTO updateUser(Long id, UserRegisterDTO dto);

    List<UserResponseDTO> searchByName(String name);

   // void deleteUser(Long id);
}