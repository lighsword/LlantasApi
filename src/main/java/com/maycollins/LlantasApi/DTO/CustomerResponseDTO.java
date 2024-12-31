package com.maycollins.LlantasApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerResponseDTO {
    private Integer customerId;
    private String customerName;
    private String customerLastname;
    private String email;
    private String phone;
    private LocalDateTime saleHistory;
    private String customerCategory;
    private String customerStatus;
    private String preferredPaymentMethod;
    private List<SaleHistoryDTO> salesHistory;
    private BigDecimal totalPurchases;
    private Integer totalTransactions;
}