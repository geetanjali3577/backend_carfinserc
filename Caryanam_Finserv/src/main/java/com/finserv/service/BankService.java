package com.finserv.service;

import com.finserv.dto.BankRequestDTO;
import com.finserv.dto.BankResponseDTO;
import com.finserv.entity.Bank;

import java.util.List;

public interface BankService {

    BankResponseDTO saveBank(BankRequestDTO dto);

    List<BankResponseDTO> getAllBanks();

    BankResponseDTO getBankById(Long id);

    BankResponseDTO updateBank(Long id,
                               BankRequestDTO dto);

    void deleteBank(Long id);

    BankResponseDTO updateStatus(Long id,
                                 String status);
}