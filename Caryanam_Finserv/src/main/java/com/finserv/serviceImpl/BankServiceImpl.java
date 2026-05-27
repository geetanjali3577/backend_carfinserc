package com.finserv.serviceImpl;

import com.finserv.dto.BankRequestDTO;
import com.finserv.dto.BankResponseDTO;
import com.finserv.entity.Bank;
import com.finserv.repository.BankRepository;
import com.finserv.service.BankService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankServiceImpl implements BankService {

    private final BankRepository bankRepository;

    @Override
    public BankResponseDTO saveBank(BankRequestDTO dto) {

        if (bankRepository.existsByBankName(
                dto.getBankName())) {

            throw new RuntimeException(
                    "Bank already exists");
        }

        Bank bank = new Bank();

        bank.setBankName(dto.getBankName());
        bank.setWhatsappNumber(dto.getWhatsappNumber());
        bank.setRoiMin(dto.getRoiMin());
        bank.setRoiMax(dto.getRoiMax());
        bank.setProcessingDays(dto.getProcessingDays());
        bank.setMaxLtv(dto.getMaxLtv());
        bank.setMaxTenureMonths(dto.getMaxTenureMonths());
        bank.setFeatures(dto.getFeatures());
        bank.setCasesThisMonth(dto.getCasesThisMonth());
        bank.setStatus(dto.getStatus());

        // ADMIN ID SAVE
        bank.setAdminId(dto.getAdminId());

        Bank savedBank = bankRepository.save(bank);

        return mapToDTO(savedBank);
    }

    @Override
    public List<BankResponseDTO> getAllBanks() {

        return bankRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BankResponseDTO getBankById(Long id) {

        Bank bank = bankRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Bank not found"));

        return mapToDTO(bank);
    }

    @Override
    public BankResponseDTO updateBank(Long id, BankRequestDTO dto) {

        Bank existingBank = bankRepository.findById(id).orElse(null);
        if (existingBank == null) {throw new RuntimeException("Bank not found");
        }
        if (dto.getBankName() != null) {existingBank.setBankName(dto.getBankName());}
        if (dto.getWhatsappNumber() != null) {existingBank.setWhatsappNumber(dto.getWhatsappNumber());
        }
        if (dto.getRoiMin() != null) {existingBank.setRoiMin(dto.getRoiMin());
        }
        if (dto.getRoiMax() != null) {existingBank.setRoiMax(dto.getRoiMax());
        }
        if (dto.getProcessingDays() != null) {existingBank.setProcessingDays(dto.getProcessingDays());
        }
        if (dto.getMaxLtv() != null) {existingBank.setMaxLtv(dto.getMaxLtv());
        }
        if (dto.getMaxTenureMonths() != null) {existingBank.setMaxTenureMonths(dto.getMaxTenureMonths());
        }
        if (dto.getFeatures() != null) {existingBank.setFeatures(dto.getFeatures());
        }
        if (dto.getCasesThisMonth() != null) {existingBank.setCasesThisMonth(dto.getCasesThisMonth());
        }
        if (dto.getStatus() != null) {existingBank.setStatus(dto.getStatus());
        }
        if (dto.getAdminId() != null) {existingBank.setAdminId(dto.getAdminId());
        }
        Bank updatedBank = bankRepository.save(existingBank);
        return mapToDTO(updatedBank);
    }
    @Override
    public void deleteBank(Long id) {
        Bank bank = bankRepository.findById(id).orElseThrow(() -> new RuntimeException("Bank not found"));
        bankRepository.delete(bank);
    }

    @Override
    public BankResponseDTO updateStatus(Long id, String status) {
        Bank bank = bankRepository.findById(id).orElseThrow(() -> new RuntimeException("Bank not found"));
        bank.setStatus(status);
        Bank updatedBank = bankRepository.save(bank);
        return mapToDTO(updatedBank);
    }

    // DTO MAPPING
    private BankResponseDTO mapToDTO(Bank bank) {

        return BankResponseDTO.builder()
                .bankId(bank.getId())
                .bankName(bank.getBankName())
                .whatsappNumber(bank.getWhatsappNumber())
                .roiMin(bank.getRoiMin())
                .roiMax(bank.getRoiMax())
                .processingDays(bank.getProcessingDays())
                .maxLtv(bank.getMaxLtv())
                .maxTenureMonths(bank.getMaxTenureMonths())
                .features(bank.getFeatures())
                .casesThisMonth(bank.getCasesThisMonth())
                .status(bank.getStatus())
                .adminId(bank.getAdminId())
                .build();
    }
}