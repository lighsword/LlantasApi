package com.maycollins.LlantasApi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid")
    private Integer productid;

    @Column(name = "productserial", nullable = false)
    private String productserial;

    @ManyToOne
    @JoinColumn(name = "categoryid", nullable = false)
    private ProductCategory productcategory;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "isdefective")
    private Boolean isdefective;

    @Column(name = "warrantyperiod")
    @Temporal(TemporalType.DATE)
    private Date warrantyperiod;

}