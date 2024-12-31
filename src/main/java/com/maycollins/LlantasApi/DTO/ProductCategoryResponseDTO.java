package com.maycollins.LlantasApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryResponseDTO {
    private Integer categoryId;
    private String categoryName;
    private String brand;
    private String model;
    private String measurement;
    private String toolType;
    private String specifications;
    private String sparePartType;
    private String compatibility;
}
