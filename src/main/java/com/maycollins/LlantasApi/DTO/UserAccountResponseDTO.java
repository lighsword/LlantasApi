package com.maycollins.LlantasApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountResponseDTO {
    private Integer userId;
    private String username;
    private String email;
    private String userRole;
    private String userStatus;
    private LocalDateTime creationDate;
    private LocalDateTime lastAccess;
    private String contactPhone;
    private String address;
    private String modulePermissions;
    private String profilePicture;
}