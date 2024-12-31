package com.maycollins.LlantasApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Integer productId;
    private String productSerial;
    private Integer categoryId;
    private Integer stock;
    private BigDecimal price;
    private Boolean isDefective;
    private LocalDate warrantyPeriod;
    private String imageUrl;  // Para el manejo de imágenes
}