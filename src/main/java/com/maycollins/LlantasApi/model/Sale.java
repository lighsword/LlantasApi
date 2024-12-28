package com.maycollins.LlantasApi.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "sale")
public class Sale {
    @Id
    private Integer saleid;
    private Date saledate;
    private Integer soldquantity;
    private Double subtotal;
    private Double taxes;
    private Double total;
    private Double discount;
    private String paymentmethod;

    @ManyToOne
    @JoinColumn(name = "customerid")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "productid")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserAccount userAccount;
}