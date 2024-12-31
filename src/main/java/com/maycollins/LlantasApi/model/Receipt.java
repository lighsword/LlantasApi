package com.maycollins.LlantasApi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "receipt")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    @Id
    @Column(name = "receipt_id")
    private Integer receiptId;

    @ManyToOne
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "issue_date", nullable = false)
    private LocalDateTime issueDate;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @Column(name = "details")
    private String details;

    @Column(name = "receipt_type", nullable = false)
    private String receiptType;

    @Column(name = "receipt_series", nullable = false)
    private String receiptSeries;

    @Column(name = "payment_method", nullable = false)
    private String paymentMethod;

    @Column(name = "receipt_status", nullable = false)
    private String receiptStatus;

    @Column(name = "billing_data", nullable = false, columnDefinition = "json")
    private String billingData;
}