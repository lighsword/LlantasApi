package com.maycollins.LlantasApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Integer customerId;
    private String customerName;
    private String customerLastname;
    private String email;
    private String phone;
    private LocalDateTime saleHistory;
    private String customerCategory;  // "Regular", "Premium", "Mayorista"
    private String customerStatus;    // "Activo", "Inactivo", "Moroso"
    private String preferredPaymentMethod;
}