package com.maycollins.LlantasApi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "purchase")
public class Purchase {
    @Id
    private Integer purchaseid;
    private Date purchasedate;
    private Integer quantity;
    private Double purchaseprice;
    private String purchasestatus;

    @ManyToOne
    @JoinColumn(name = "productid")
    private Product product;
}