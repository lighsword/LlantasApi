package com.maycollins.LlantasApi.service;

import com.maycollins.LlantasApi.DTO.UserAccountDTO;
import com.maycollins.LlantasApi.DTO.UserAccountResponseDTO;
import com.maycollins.LlantasApi.model.UserAccount;
import com.maycollins.LlantasApi.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserAccountService(UserAccountRepository userAccountRepository,
                              PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserAccountResponseDTO createUser(UserAccountDTO userDTO) {
        validateUserUniqueness(userDTO.getEmail(), userDTO.getUsername());
        try {
            UserAccount user = createUserFromDTO(userDTO);
            UserAccount savedUser = userAccountRepository.save(user);
            return convertToResponseDTO(savedUser);
        } catch (Exception e) {
            throw new RuntimeException("Error creating user: " + e.getMessage());
        }
    }

    public UserAccountResponseDTO getUserById(Integer id) {
        return convertToResponseDTO(findUserById(id));
    }

    public List<UserAccountResponseDTO> getAllUsers() {
        return userAccountRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public UserAccountResponseDTO updateUser(Integer id, UserAccountDTO userDTO) {
        UserAccount user = findUserById(id);
        validateEmailForUpdate(user, userDTO.getEmail());
        updateUserFields(user, userDTO);
        UserAccount updatedUser = userAccountRepository.save(user);
        return convertToResponseDTO(updatedUser);
    }

    public void deleteUser(Integer id) {
        if (!userAccountRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userAccountRepository.deleteById(id);
    }

    private UserAccount findUserById(Integer id) {
        return userAccountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    private void validateUserUniqueness(String email, String username) {
        if (userAccountRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }
        if (userAccountRepository.existsByUsername(username)) {
            throw new RuntimeException("Username already exists");
        }
    }

    private void validateEmailForUpdate(UserAccount user, String newEmail) {
        if (!user.getEmail().equals(newEmail) &&
                userAccountRepository.existsByEmail(newEmail)) {
            throw new RuntimeException("Email already exists");
        }
    }

    private UserAccount createUserFromDTO(UserAccountDTO userDTO) {
        UserAccount user = new UserAccount();
        user.setUserId(userDTO.getUserId());
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setUserRole(userDTO.getUserRole());
        user.setUserStatus("active");
        user.setCreationDate(LocalDateTime.now());
        user.setLastAccess(LocalDateTime.now());
        user.setContactPhone(userDTO.getContactPhone());
        user.setAddress(userDTO.getAddress());
        user.setModulePermissions(userDTO.getModulePermissions());
        user.setProfilePicture(userDTO.getProfilePicture());
        return user;
    }

    private void updateUserFields(UserAccount user, UserAccountDTO userDTO) {
        user.setUsername(userDTO.getUsername());
        user.setEmail(userDTO.getEmail());
        if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        }
        user.setUserRole(userDTO.getUserRole());
        user.setUserStatus(userDTO.getUserStatus());
        user.setLastAccess(LocalDateTime.now());
        user.setContactPhone(userDTO.getContactPhone());
        user.setAddress(userDTO.getAddress());
        user.setModulePermissions(userDTO.getModulePermissions());
        user.setProfilePicture(userDTO.getProfilePicture());
    }

    private UserAccountResponseDTO convertToResponseDTO(UserAccount user) {
        UserAccountResponseDTO dto = new UserAccountResponseDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setUserRole(user.getUserRole());
        dto.setUserStatus(user.getUserStatus());
        dto.setCreationDate(user.getCreationDate());
        dto.setLastAccess(user.getLastAccess());
        dto.setContactPhone(user.getContactPhone());
        dto.setAddress(user.getAddress());
        dto.setModulePermissions(user.getModulePermissions());
        dto.setProfilePicture(user.getProfilePicture());
        return dto;
    }
}