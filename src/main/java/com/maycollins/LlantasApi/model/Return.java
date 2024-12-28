package com.maycollins.LlantasApi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@Table(name = "return") // Note: "return" is a reserved word in Java, might want to rename the table
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Return {
    @Id
    @Column(name = "return_id")
    private Integer returnId;

    @ManyToOne
    @JoinColumn(name = "warehouse_id", nullable = false)
    private WarehouseInventory warehouse;

    @ManyToOne
    @JoinColumn(name = "sale_id", nullable = false)
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "defective_quantity", nullable = false)
    private Integer defectiveQuantity;

    @Column(name = "return_registration_date", nullable = false)
    private LocalDateTime returnRegistrationDate;

    @Column(name = "return_change_date")
    private LocalDateTime returnChangeDate;

    @Column(name = "return_finalization_date")
    private LocalDateTime returnFinalizationDate;

    @Column(name = "return_status", nullable = false)
    private Boolean returnStatus;
}