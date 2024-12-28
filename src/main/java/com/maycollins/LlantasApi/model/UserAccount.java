package com.maycollins.LlantasApi.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "useraccount")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "useraccount_userid_seq")
    @SequenceGenerator(name = "useraccount_userid_seq", sequenceName = "useraccount_userid_seq", allocationSize = 1)
    @Column(name = "userid", nullable = false, updatable = false)
    private Long userId;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "userrole", nullable = false)
    private String userRole;

    @Column(name = "userstatus", nullable = false)
    private String userStatus = "ACTIVE";

    @Column(name = "creationdate", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate = new Date();

    @Column(name = "lastaccess")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastAccess;

    @Column(name = "contactphone")
    private String contactPhone;

    @Column(name = "address")
    private String address;

    @Column(name = "modulepermissions", columnDefinition = "jsonb")
    private String modulePermissionsJson;

    @Column(name = "profilepicture")
    private String profilePicture;

    @Transient
    private Map<String, List<String>> modulePermissions;

    @PrePersist
    protected void onCreate() {
        creationDate = new Date();
        if (userStatus == null) {
            userStatus = "ACTIVE";
        }
    }

    @PreUpdate
    protected void onUpdate() {
        lastAccess = new Date();
    }
}