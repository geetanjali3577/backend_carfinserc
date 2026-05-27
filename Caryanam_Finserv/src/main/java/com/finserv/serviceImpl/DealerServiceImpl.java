package com.finserv.serviceImpl;

import com.finserv.dto.DealerRegisterDTO;
import com.finserv.dto.DealerResponseDTO;
import com.finserv.entity.Dealer;
import com.finserv.enums.DealerStatus;
import com.finserv.enums.Role;
import com.finserv.repository.DealerRepository;
import com.finserv.repository.UserRepository;
import com.finserv.service.DealerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class DealerServiceImpl implements DealerService {

    @Autowired
    private DealerRepository dealerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ================= REGISTER DEALER =================
    @Override
    public DealerResponseDTO registerDealer(DealerRegisterDTO dto) {

        if (dealerRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email Already Exists");
        }
        if (dealerRepository.existsByMobileNumber(dto.getMobileNumber())) {
            throw new RuntimeException("Mobile Number Already Exists");
        }

        Dealer dealer = new Dealer();
        dealer.setFullName(dto.getFullName());
        dealer.setEmail(dto.getEmail());
        dealer.setMobileNumber(dto.getMobileNumber());
        dealer.setPassword(passwordEncoder.encode(dto.getPassword()));
        dealer.setRole(Role.DEALER);
        dealer.setStatus(DealerStatus.ACTIVE);
        dealer.setCreatedAt(LocalDateTime.now());

        Dealer savedDealer = dealerRepository.save(dealer);
        return DealerResponseDTO.builder()
                .dealerId(savedDealer.getDealerId())
                .dealerCode(savedDealer.getDealerCode())
                .fullName(savedDealer.getFullName())
                .email(savedDealer.getEmail())
                .mobileNumber(savedDealer.getMobileNumber())
                .role(savedDealer.getRole().name())
                .createdAt(savedDealer.getCreatedAt())
                .build();
    }


}