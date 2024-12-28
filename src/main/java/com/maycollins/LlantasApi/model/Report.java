package com.maycollins.LlantasApi.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "report")
public class Report {
    @Id
    private Integer reportid;
    private Date generateddate;
    private String reporttype;
    private String title;
    private String description;
    @Column(columnDefinition = "json")
    private String content;
    private String category;

    @ManyToOne
    @JoinColumn(name = "userid")
    private UserAccount userAccount;

    @ManyToOne
    @JoinColumn(name = "saleid")
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "productid")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "warehouseid")
    private WarehouseInventory warehouseInventory;
}