package com.maycollins.LlantasApi.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WarehouseInventoryDTO {
    private Integer warehouseId;
    private String warehouseName;
    private String location;
    private Integer productId;
    private Integer availableQuantity;
    private Integer maxCapacity;
    private String warehouseStatus;
}