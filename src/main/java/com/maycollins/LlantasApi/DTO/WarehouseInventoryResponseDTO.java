package com.maycollins.LlantasApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseInventoryResponseDTO {
    private Integer warehouseId;
    private String warehouseName;
    private String location;
    private ProductResponseDTO product;
    private Integer availableQuantity;
    private Integer maxCapacity;
    private String warehouseStatus;
    private Boolean lowStockAlert;
    private Integer stockPercentage;
}