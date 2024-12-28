package com.maycollins.LlantasApi.model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "useraccount")
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid")
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "userrole", nullable = false)
    private String userRole; // "admin", "seller", "warehouse", "supervisor"

    @Column(name = "userstatus", nullable = false)
    private String userStatus; // "active", "inactive", "blocked"

    @Column(name = "creationdate", nullable = false)
    @Temporal(TemporalType.DATE)
    private Date creationDate;

    @Column(name = "lastaccess")
    @Temporal(TemporalType.DATE)
    private Date lastAccess;

    @Column(name = "contactphone")
    private String contactPhone;

    @Column(name = "address")
    private String address;

    @Column(name = "modulepermissions", columnDefinition = "json")
    private String modulePermissions; // JSON string for permissions

    @Column(name = "profilepicture")
    private String profilePicture; // URL or path to the profile picture

    // Getters and Setters
    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserRole() {
        return userRole;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastAccess() {
        return lastAccess;
    }

    public void setLastAccess(Date lastAccess) {
        this.lastAccess = lastAccess;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getModulePermissions() {
        return modulePermissions;
    }

    public void setModulePermissions(String modulePermissions) {
        this.modulePermissions = modulePermissions;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }
}