package com.maycollins.LlantasApi.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "productid")
    private Long productId;

    @Column(name = "productserial", nullable = false)
    private String productSerial;

    @ManyToOne
    @JoinColumn(name = "categoryid", nullable = false)
    private ProductCategory productCategory;

    @Column(name = "stock", nullable = false)
    private Integer stock;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "isdefective")
    private Boolean isDefective;

    @Column(name = "warrantyperiod")
    @Temporal(TemporalType.DATE)
    private Date warrantyPeriod;

    // Getters y Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductSerial() {
        return productSerial;
    }

    public void setProductSerial(String productSerial) {
        this.productSerial = productSerial;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Boolean getIsDefective() {
        return isDefective;
    }

    public void setIsDefective(Boolean isDefective) {
        this.isDefective = isDefective;
    }

    public Date getWarrantyPeriod() {
        return warrantyPeriod;
    }

    public void setWarrantyPeriod(Date warrantyPeriod) {
        this.warrantyPeriod = warrantyPeriod;
    }
}