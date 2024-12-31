package com.maycollins.LlantasApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountDTO {
    private Integer userId;
    private String username;
    private String email;
    private String password;
    private String userRole;
    private String userStatus;
    private String contactPhone;
    private String address;
    private String modulePermissions;
    private String profilePicture;
}