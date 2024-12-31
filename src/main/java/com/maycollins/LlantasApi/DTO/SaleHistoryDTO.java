package com.maycollins.LlantasApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaleHistoryDTO {
    private Integer saleId;
    private LocalDateTime saleDate;
    private String productName;
    private Integer quantity;
    private BigDecimal total;
    private String paymentMethod;
    private String saleStatus;
}