package com.maycollins.LlantasApi.repository;

import com.maycollins.LlantasApi.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Integer> {
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
    Optional<UserAccount> findByEmail(String email);
}