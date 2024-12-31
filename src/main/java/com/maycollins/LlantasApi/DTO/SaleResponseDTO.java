package com.maycollins.LlantasApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleResponseDTO {
    private Integer saleId;
    private LocalDateTime saleDate;
    private CustomerResponseDTO customer;
    private ProductResponseDTO product;
    private Integer soldQuantity;
    private BigDecimal subtotal;
    private BigDecimal taxes;
    private BigDecimal total;
    private BigDecimal discount;
    private UserAccountResponseDTO user;
    private String paymentMethod;
    private String saleStatus;
}