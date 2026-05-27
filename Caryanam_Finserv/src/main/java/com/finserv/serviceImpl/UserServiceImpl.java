package com.finserv.serviceImpl;

import com.finserv.dto.UserRegisterDTO;
import com.finserv.dto.UserResponseDTO;
import com.finserv.entity.User;
import com.finserv.enums.RegistrationType;
import com.finserv.enums.Role;
import com.finserv.enums.UserStatus;
import com.finserv.repository.DealerRepository;
import com.finserv.repository.UserRepository;
import com.finserv.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DealerRepository dealerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserResponseDTO registerUser(UserRegisterDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email Already Exists");
        }
        if (userRepository.existsByMobileNumber(dto.getMobileNumber())) {
            throw new RuntimeException("Mobile Number Already Exists");
        }
        if (dto.getRegistrationType() == RegistrationType.DEALER) {
            if (dto.getDealerCode() == null || dto.getDealerCode().trim().isEmpty()) {
                throw new RuntimeException("Dealer Code is Required");
            }
            if (!dealerRepository.existsByDealerCode(dto.getDealerCode())) {
                throw new RuntimeException("Invalid Dealer Code");
            }
        }
        User user = new User();
        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail().toLowerCase().trim());
        user.setMobileNumber(dto.getMobileNumber());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRegistrationType(dto.getRegistrationType());

        // Save Dealer Code only if DEALER type
        if (dto.getRegistrationType() == RegistrationType.DEALER) {
            user.setDealerCode(dto.getDealerCode());}
        user.setRole(Role.USER);
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);


        UserResponseDTO response = new UserResponseDTO();
        response.setUserId(savedUser.getUserId());
        response.setFullName(savedUser.getFullName());
        response.setEmail(savedUser.getEmail());
        response.setMobileNumber(savedUser.getMobileNumber());
        response.setRegistrationType(savedUser.getRegistrationType().name());
        response.setDealerCode(savedUser.getDealerCode());
        response.setRole(savedUser.getRole().name());
        response.setCreatedAt(savedUser.getCreatedAt());
        return response;
    }
}