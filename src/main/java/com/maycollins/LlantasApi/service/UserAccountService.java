package com.maycollins.LlantasApi.service;

import com.maycollins.LlantasApi.model.UserAccount;
import com.maycollins.LlantasApi.repository.UserAccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserAccountService {

    private final UserAccountRepository userAccountRepository;

    public UserAccountService(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public List<UserAccount> getAllUsers() {
        return userAccountRepository.findAll();
    }

    public Optional<UserAccount> getUserById(Long userId) {
        return userAccountRepository.findById(userId);
    }

    public UserAccount createUser(UserAccount userAccount) {
        return userAccountRepository.save(userAccount);
    }

    public UserAccount updateUser(Long userId, UserAccount userDetails) {
        return userAccountRepository.findById(userId).map(user -> {
            user.setEmail(userDetails.getEmail());
            user.setPassword(userDetails.getPassword());
            user.setUserRole(userDetails.getUserRole());
            user.setUserStatus(userDetails.getUserStatus());
            user.setContactPhone(userDetails.getContactPhone());
            user.setAddress(userDetails.getAddress());
            user.setProfilePicture(userDetails.getProfilePicture());
            return userAccountRepository.save(user);
        }).orElseThrow(() -> new RuntimeException("User not found with id " + userId));
    }

    public void deleteUser(Long userId) {
        userAccountRepository.deleteById(userId);
    }
}