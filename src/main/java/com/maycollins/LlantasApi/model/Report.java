package com.maycollins.LlantasApi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "report")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Report {
    @Id
    @Column(name = "report_id")
    private Integer reportId;

    @Column(name = "generated_date", nullable = false)
    private LocalDateTime generatedDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserAccount userAccount;

    @ManyToOne
    @JoinColumn(name = "sale_id")
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "warehouse_id")
    private WarehouseInventory warehouse;

    @Column(name = "report_type", nullable = false)
    private String reportType;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "content", nullable = false, columnDefinition = "json")
    private String content;

    @Column(name = "category", nullable = false)
    private String category;
}