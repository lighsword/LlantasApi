package com.maycollins.LlantasApi.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product_category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategory {
    @Id
    @Column(name = "category_id")
    private Integer categoryId;

    @Column(name = "category_name", nullable = false)
    private String categoryName;

    @Column(name = "brand", nullable = false)
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "measurement")
    private String measurement;

    @Column(name = "tool_type")
    private String toolType;

    @Column(name = "specifications")
    private String specifications;

    @Column(name = "spare_part_type")
    private String sparePartType;

    @Column(name = "compatibility")
    private String compatibility;
}