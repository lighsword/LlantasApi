package com.maycollins.LlantasApi.controller;

import com.maycollins.LlantasApi.DTO.WarehouseInventoryDTO;
import com.maycollins.LlantasApi.DTO.WarehouseInventoryResponseDTO;
import com.maycollins.LlantasApi.service.WarehouseInventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/warehouse-inventory")
@CrossOrigin(origins = "*")
public class WarehouseInventoryController {

    private final WarehouseInventoryService warehouseInventoryService;

    public WarehouseInventoryController(WarehouseInventoryService warehouseInventoryService) {
        this.warehouseInventoryService = warehouseInventoryService;
    }

    @PostMapping
    public ResponseEntity<WarehouseInventoryResponseDTO> createWarehouseInventory(
            @RequestBody WarehouseInventoryDTO dto) {
        return ResponseEntity.ok(warehouseInventoryService.createWarehouseInventory(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<WarehouseInventoryResponseDTO> getWarehouseInventoryById(
            @PathVariable Integer id) {
        return ResponseEntity.ok(warehouseInventoryService.getWarehouseInventoryById(id));
    }

    @GetMapping
    public ResponseEntity<List<WarehouseInventoryResponseDTO>> getAllWarehouseInventories() {
        return ResponseEntity.ok(warehouseInventoryService.getAllWarehouseInventories());
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<WarehouseInventoryResponseDTO>> getLowStockItems() {
        return ResponseEntity.ok(warehouseInventoryService.getLowStockItems());
    }

    @GetMapping("/low-stock-percentage")
    public ResponseEntity<List<WarehouseInventoryResponseDTO>> getLowStockPercentage() {
        return ResponseEntity.ok(warehouseInventoryService.getLowStockPercentage());
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<WarehouseInventoryResponseDTO> updateStock(
            @PathVariable Integer id,
            @RequestParam Integer quantity) {
        return ResponseEntity.ok(warehouseInventoryService.updateStock(id, quantity));
    }

    @PutMapping("/{id}")
    public ResponseEntity<WarehouseInventoryResponseDTO> updateWarehouseInventory(
            @PathVariable Integer id,
            @RequestBody WarehouseInventoryDTO dto) {
        return ResponseEntity.ok(warehouseInventoryService.updateWarehouseInventory(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWarehouseInventory(@PathVariable Integer id) {
        warehouseInventoryService.deleteWarehouseInventory(id);
        return ResponseEntity.ok().build();
    }
}