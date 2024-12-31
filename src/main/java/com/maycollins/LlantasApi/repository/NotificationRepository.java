package com.maycollins.LlantasApi.repository;

import com.maycollins.LlantasApi.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    List<Notification> findByUserAccountUserIdOrderBySendDateDesc(Integer userId);
    List<Notification> findByUserAccountUserIdAndStatus(Integer userId, String status);
    long countByUserAccountUserIdAndStatus(Integer userId, String status);
}