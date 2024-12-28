package com.maycollins.LlantasApi.model;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "customer")
public class Customer {
    @Id
    private Integer customerid;
    private String customername;
    private String customerlastname;
    private String email;
    private String phone;
    private Date salehistory;
    private String customercategory;
    private String customerstatus;
    private String preferredpaymentmethod;
}