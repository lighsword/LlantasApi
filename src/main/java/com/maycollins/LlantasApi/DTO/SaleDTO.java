package com.maycollins.LlantasApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleDTO {
    private Integer saleId;
    private LocalDateTime saleDate;
    private Integer customerId;
    private Integer productId;
    private Integer soldQuantity;
    private BigDecimal subtotal;
    private BigDecimal taxes;
    private BigDecimal total;
    private BigDecimal discount;
    private Integer userId;
    private String paymentMethod;
}