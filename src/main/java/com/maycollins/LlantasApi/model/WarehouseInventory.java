package com.maycollins.LlantasApi.model;

import jakarta.persistence.*;
import lombok.Data;


@Data
@Entity
@Table(name = "warehouseinventory")
public class WarehouseInventory {
    @Id
    private Integer warehouseid;
    private String warehousename;
    private String location;
    private Integer availablequantity;
    private Integer maxcapacity;
    private String warehousestatus;

    @ManyToOne
    @JoinColumn(name = "productid")
    private Product product;
}