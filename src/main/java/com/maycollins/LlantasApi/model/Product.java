package com.maycollins.LlantasApi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

    @Column(name = "serial_number", nullable = false, unique = true)
    private String serialNumber;

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    @Column(name = "is_defective")
    private Boolean isDefective;

    @Column(name = "warranty_period")
    private LocalDateTime warrantyPeriod;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategory category;
}