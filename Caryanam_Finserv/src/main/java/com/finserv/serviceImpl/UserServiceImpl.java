package com.finserv.serviceImpl;

import com.finserv.dto.UserRegisterDTO;
import com.finserv.dto.UserResponseDTO;
import com.finserv.entity.PersonalInfo;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DealerRepository dealerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //====================================================
    // GENERATE APPLICATION ID
    //====================================================
    @Override
    public String generateApplicationId() {

        Optional<User> lastUser =
                userRepository.findTopByOrderByUserIdDesc();

        int nextNumber = 1;

        if (lastUser.isPresent()
                && lastUser.get().getApplicationId() != null) {

            String lastApplicationId =
                    lastUser.get().getApplicationId();

            String numberPart =
                    lastApplicationId.substring(7);

            nextNumber =
                    Integer.parseInt(numberPart) + 1;
        }

        return String.format(
                "CRY2026%04d",
                nextNumber
        );
    }

    //====================================================
    // REGISTER USER
    //====================================================
    @Override
    public UserResponseDTO registerUser(UserRegisterDTO dto) {

        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email Already Exists");
        }

        if (userRepository.existsByMobileNumber(
                dto.getMobileNumber())) {

            throw new RuntimeException(
                    "Mobile Number Already Exists"
            );
        }

        if (dto.getRegistrationType()
                == RegistrationType.DEALER) {

            if (dto.getDealerCode() == null
                    || dto.getDealerCode()
                    .trim()
                    .isEmpty()) {

                throw new RuntimeException(
                        "Dealer Code is Required"
                );
            }

            if (!dealerRepository.existsByDealerCode(
                    dto.getDealerCode())) {

                throw new RuntimeException(
                        "Invalid Dealer Code"
                );
            }
        }

        User user = new User();

        user.setFullName(dto.getFullName());

        user.setEmail(
                dto.getEmail()
                        .toLowerCase()
                        .trim()
        );

        user.setMobileNumber(dto.getMobileNumber());

        user.setPassword(
                passwordEncoder.encode(
                        dto.getPassword()
                )
        );

        user.setRegistrationType(
                dto.getRegistrationType()
        );

        // AUTO APPLICATION ID
        user.setApplicationId(
                generateApplicationId()
        );

        // SAVE DEALER CODE
        if (dto.getRegistrationType()
                == RegistrationType.DEALER) {

            user.setDealerCode(
                    dto.getDealerCode()
            );
        }

        user.setRole(Role.USER);

        user.setStatus(UserStatus.ACTIVE);

        user.setCreatedAt(LocalDateTime.now());

        User savedUser =
                userRepository.save(user);

        UserResponseDTO response =
                new UserResponseDTO();

        response.setUserId(
                savedUser.getUserId()
        );

        response.setApplicationId(
                savedUser.getApplicationId()
        );

        response.setFullName(
                savedUser.getFullName()
        );

        response.setEmail(
                savedUser.getEmail()
        );

        response.setMobileNumber(
                savedUser.getMobileNumber()
        );

        response.setRegistrationType(
                savedUser.getRegistrationType().name()
        );

        response.setDealerCode(
                savedUser.getDealerCode()
        );

        response.setRole(
                savedUser.getRole().name()
        );

        response.setCreatedAt(
                savedUser.getCreatedAt()
        );

        return response;
    }

    @Override
    public UserResponseDTO getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        UserResponseDTO dto = new UserResponseDTO();

        // USER DATA
        dto.setUserId(user.getUserId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setApplicationId(user.getApplicationId());
        dto.setDealerCode(user.getDealerCode());
        dto.setRegistrationType(
                user.getRegistrationType() != null ? user.getRegistrationType().name() : null
        );
        dto.setRole(user.getRole() != null ? user.getRole().name() : null);
        dto.setCreatedAt(user.getCreatedAt());

        // PERSONAL INFO (SAFE NULL CHECK)
        if (user.getPersonalInfo() != null) {
            dto.setMobileNumber(user.getPersonalInfo().getMobileNumber());
        }

        return dto;
    }
    @Override
    public List<UserResponseDTO> getAllUsers() {

        List<User> users = userRepository.findAllUsersWithPersonalInfo();

        return users.stream().map(user -> {

            UserResponseDTO dto = new UserResponseDTO();

            // USER DATA
            dto.setUserId(user.getUserId());
            dto.setFullName(user.getFullName());
            dto.setEmail(user.getEmail());
            dto.setRegistrationType(
                    user.getRegistrationType() != null ? user.getRegistrationType().name() : null
            );
            dto.setRole(user.getRole() != null ? user.getRole().name() : null);
            dto.setApplicationId(user.getApplicationId());
            dto.setDealerCode(user.getDealerCode());
            dto.setCreatedAt(user.getCreatedAt());

            //  PERSONAL INFO (JOIN DATA)
            if (user.getPersonalInfo() != null) {
                dto.setMobileNumber(user.getPersonalInfo().getMobileNumber());
            }

            return dto;

        }).toList();
    }
    @Override
    public UserResponseDTO updateUser(Long id, UserRegisterDTO dto) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        // ======================
        // USER UPDATE
        // ======================
        if (dto.getFullName() != null && !dto.getFullName().isEmpty()) {
            user.setFullName(dto.getFullName());
        }

        if (dto.getEmail() != null && !dto.getEmail().isEmpty()) {
            user.setEmail(dto.getEmail());
        }

        if (dto.getPassword() != null && !dto.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        if (dto.getDealerCode() != null && !dto.getDealerCode().isEmpty()) {
            user.setDealerCode(dto.getDealerCode());
        }

        if (dto.getRegistrationType() != null) {
            user.setRegistrationType(dto.getRegistrationType());
        }

        if (dto.getRole() != null) {
            user.setRole(Role.valueOf(dto.getRole()));
        }

        // ======================
        // PERSONAL INFO SYNC UPDATE
        // ======================
        if (user.getPersonalInfo() != null) {

            PersonalInfo info = user.getPersonalInfo();

            // sync fields from User → PersonalInfo
            if (dto.getFullName() != null) {
                info.setFullName(dto.getFullName());
            }

            if (dto.getEmail() != null) {
                info.setEmail(dto.getEmail());
            }

            if (dto.getMobileNumber() != null) {
                info.setMobileNumber(dto.getMobileNumber());
            }
        }

        userRepository.save(user);

        return mapToDTO(user);
    }
  /*  @Override
    public void deleteUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        userRepository.delete(user);
    }
*/
    // mapper
    private UserResponseDTO mapToDTO(User user) {

        UserResponseDTO dto = new UserResponseDTO();

        // ======================
        // USER FIELDS
        // ======================
        dto.setUserId(user.getUserId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setApplicationId(user.getApplicationId());
        dto.setDealerCode(user.getDealerCode());

        dto.setRegistrationType(
                user.getRegistrationType() != null
                        ? user.getRegistrationType().name()
                        : null
        );

        dto.setRole(
                user.getRole() != null
                        ? user.getRole().name()
                        : null
        );

        dto.setCreatedAt(user.getCreatedAt());

        // ======================
        // PERSONAL INFO (IMPORTANT)
        // ======================
        dto.setMobileNumber(
                user.getPersonalInfo() != null
                        ? user.getPersonalInfo().getMobileNumber()
                        : null
        );

        return dto;
    }

}