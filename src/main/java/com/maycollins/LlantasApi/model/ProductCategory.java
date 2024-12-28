package com.maycollins.LlantasApi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "productcategory")
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "categoryid")
    private Integer categoryId;

    @Column(name = "categoryname", nullable = false)
    private String categoryName;

    @Column(name = "brand")
    private String brand;

    @Column(name = "model")
    private String model;

    @Column(name = "measurement")
    private String measurement;

    @Column(name = "tooltype")
    private String toolType;

    @Column(name = "specifications")
    private String specifications;

    @Column(name = "spareparttype")
    private String sparePartType;

    @Column(name = "compatibility")
    private String compatibility;

    @OneToMany(mappedBy = "productCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Product> products;

}