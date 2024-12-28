package com.maycollins.LlantasApi.model;


import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "notification")
public class Notification {

    @Id
    private Integer notificationid;
    private String notificationtype;
    private String title;
    private String notificationmessage;
    private String priority;
    private String status;
    private Date senddate;
    private Date readdate;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserAccount userAccount;
}