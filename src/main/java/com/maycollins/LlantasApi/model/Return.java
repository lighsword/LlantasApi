package com.maycollins.LlantasApi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.Date;

@Data
@Entity
@Table(name = "return")
public class Return {
    @Id
    private Integer returnid;
    private Integer defectivequantity;
    private Date returnregistrationdate;
    private Date returnchangedate;
    private Date returnfinalizationdate;
    private Boolean returnstatus;

    @ManyToOne
    @JoinColumn(name = "warehouseid")
    private WarehouseInventory warehouseInventory;

    @ManyToOne
    @JoinColumn(name = "saleid")
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "productid")
    private Product product;
}