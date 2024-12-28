package com.maycollins.LlantasApi.model;

import lombok.Data;
import jakarta.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "receipt")
public class Receipt {
    @Id
    private Integer receiptid;
    private Date issuedate;
    private Double total;
    private String details;
    private String receipttype;
    private String receiptseries;
    private String paymentmethod;
    private String receiptstatus;
    @Column(columnDefinition = "json")
    private String billingdata;

    @ManyToOne
    @JoinColumn(name = "saleid")
    private Sale sale;

    @ManyToOne
    @JoinColumn(name = "customerid")
    private Customer customer;
}