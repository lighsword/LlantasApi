package com.maycollins.LlantasApi.repository;

import com.maycollins.LlantasApi.model.WarehouseInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface WarehouseInventoryRepository extends JpaRepository<WarehouseInventory, Integer> {
    List<WarehouseInventory> findByProductProductId(Integer productId);

    List<WarehouseInventory> findByWarehouseStatus(String status);

    @Query("SELECT w FROM WarehouseInventory w WHERE w.availableQuantity <= :threshold")
    List<WarehouseInventory> findLowStockItems(@Param("threshold") Integer threshold);

    @Query("SELECT w FROM WarehouseInventory w WHERE (w.availableQuantity * 100.0 / w.maxCapacity) <= :percentageThreshold")
    List<WarehouseInventory> findLowStockPercentage(@Param("percentageThreshold") Double percentageThreshold);

    List<WarehouseInventory> findByLocation(String location);

    boolean existsByWarehouseName(String warehouseName);
}