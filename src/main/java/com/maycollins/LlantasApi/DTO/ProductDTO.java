package com.maycollins.LlantasApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Integer productId;
    private String serialNumber;
    private BigDecimal price;
    private Boolean isDefective;
    private LocalDateTime warrantyPeriod;
    private Integer categoryId;
}