package com.maycollins.LlantasApi.repository;

import com.maycollins.LlantasApi.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
}