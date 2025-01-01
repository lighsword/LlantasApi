package com.maycollins.LlantasApi.security;

import com.maycollins.LlantasApi.DTO.NotificationDTO;
import com.maycollins.LlantasApi.model.Notification;
import com.maycollins.LlantasApi.model.UserAccount;
import com.maycollins.LlantasApi.repository.NotificationRepository;
import com.maycollins.LlantasApi.repository.UserAccountRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserAccountRepository userAccountRepository;

    public NotificationService(NotificationRepository notificationRepository,
                               UserAccountRepository userAccountRepository) {
        this.notificationRepository = notificationRepository;
        this.userAccountRepository = userAccountRepository;
    }

    public NotificationDTO createNotification(NotificationDTO dto) {
        UserAccount user = userAccountRepository.findById(dto.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Notification notification = new Notification();
        notification.setNotificationId(dto.getNotificationId());
        notification.setNotificationType(dto.getNotificationType());
        notification.setTitle(dto.getTitle());
        notification.setNotificationMessage(dto.getNotificationMessage());
        notification.setPriority(dto.getPriority());
        notification.setStatus("No leida");
        notification.setSendDate(LocalDateTime.now());
        notification.setUserAccount(user);

        return convertToDTO(notificationRepository.save(notification));
    }

    public List<NotificationDTO> getUserNotifications(Integer userId) {
        return notificationRepository.findByUserAccountUserIdOrderBySendDateDesc(userId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public NotificationDTO markAsRead(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setStatus("Leida");
        notification.setReadDate(LocalDateTime.now());

        return convertToDTO(notificationRepository.save(notification));
    }

    public NotificationDTO archiveNotification(Integer notificationId) {
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        notification.setStatus("Archivada");

        return convertToDTO(notificationRepository.save(notification));
    }

    public void deleteNotification(Integer notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    private NotificationDTO convertToDTO(Notification notification) {
        return new NotificationDTO(
                notification.getNotificationId(),
                notification.getNotificationType(),
                notification.getTitle(),
                notification.getNotificationMessage(),
                notification.getPriority(),
                notification.getStatus(),
                notification.getSendDate(),
                notification.getReadDate(),
                notification.getUserAccount().getUserId()
        );
    }
}