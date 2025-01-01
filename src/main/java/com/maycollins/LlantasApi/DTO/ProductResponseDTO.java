package com.maycollins.LlantasApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDTO {
    private Integer productId;
    private String productSerial;
    private ProductCategoryResponseDTO category;
    private Integer stock;
    private BigDecimal price;
    private Boolean isDefective;
    private LocalDate warrantyPeriod;
    private String imageUrl;
    private Boolean stockAlert;  // Para indicar si el stock está bajo
}