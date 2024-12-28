package com.maycollins.LlantasApi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "customer_name", nullable = false)
    private String customerName;

    @Column(name = "customer_lastname", nullable = false)
    private String customerLastname;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "sale_history")
    private LocalDateTime saleHistory;

    @Column(name = "customer_category", nullable = false)
    private String customerCategory;

    @Column(name = "customer_status", nullable = false)
    private String customerStatus;

    @Column(name = "preferred_payment_method")
    private String preferredPaymentMethod;
}