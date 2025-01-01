package com.maycollins.LlantasApi.service;

import com.maycollins.LlantasApi.DTO.ProductCategoryResponseDTO;
import com.maycollins.LlantasApi.DTO.ProductResponseDTO;
import com.maycollins.LlantasApi.DTO.WarehouseInventoryDTO;
import com.maycollins.LlantasApi.DTO.WarehouseInventoryResponseDTO;
import com.maycollins.LlantasApi.model.ProductCategory;
import com.maycollins.LlantasApi.model.WarehouseInventory;
import com.maycollins.LlantasApi.model.Product;
import com.maycollins.LlantasApi.repository.WarehouseInventoryRepository;
import com.maycollins.LlantasApi.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WarehouseInventoryService {

    private final WarehouseInventoryRepository warehouseInventoryRepository;
    private final ProductRepository productRepository;
    private final Integer LOW_STOCK_THRESHOLD = 10;
    private final Double LOW_STOCK_PERCENTAGE = 20.0;

    public WarehouseInventoryService(WarehouseInventoryRepository warehouseInventoryRepository,
                                     ProductRepository productRepository) {
        this.warehouseInventoryRepository = warehouseInventoryRepository;
        this.productRepository = productRepository;
    }

    public WarehouseInventoryResponseDTO createWarehouseInventory(WarehouseInventoryDTO dto) {
        validateWarehouse(dto);
        WarehouseInventory warehouse = convertToEntity(dto);
        WarehouseInventory savedWarehouse = warehouseInventoryRepository.save(warehouse);
        return convertToResponseDTO(savedWarehouse);
    }

    public WarehouseInventoryResponseDTO getWarehouseInventoryById(Integer id) {
        WarehouseInventory warehouse = findWarehouseById(id);
        return convertToResponseDTO(warehouse);
    }

    public List<WarehouseInventoryResponseDTO> getAllWarehouseInventories() {
        return warehouseInventoryRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<WarehouseInventoryResponseDTO> getLowStockItems() {
        return warehouseInventoryRepository.findLowStockItems(LOW_STOCK_THRESHOLD)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<WarehouseInventoryResponseDTO> getLowStockPercentage() {
        return warehouseInventoryRepository.findLowStockPercentage(LOW_STOCK_PERCENTAGE)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public WarehouseInventoryResponseDTO updateStock(Integer id, Integer quantity) {
        WarehouseInventory warehouse = findWarehouseById(id);
        validateStockUpdate(warehouse, quantity);
        warehouse.setAvailableQuantity(warehouse.getAvailableQuantity() + quantity);
        WarehouseInventory updatedWarehouse = warehouseInventoryRepository.save(warehouse);
        return convertToResponseDTO(updatedWarehouse);
    }

    public WarehouseInventoryResponseDTO updateWarehouseInventory(Integer id, WarehouseInventoryDTO dto) {
        WarehouseInventory warehouse = findWarehouseById(id);
        validateWarehouseForUpdate(warehouse, dto);
        updateWarehouseFields(warehouse, dto);
        WarehouseInventory updatedWarehouse = warehouseInventoryRepository.save(warehouse);
        return convertToResponseDTO(updatedWarehouse);
    }

    public void deleteWarehouseInventory(Integer id) {
        WarehouseInventory warehouse = findWarehouseById(id);
        warehouse.setWarehouseStatus("Inactivo");
        warehouseInventoryRepository.save(warehouse);
    }

    private WarehouseInventory findWarehouseById(Integer id) {
        return warehouseInventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Warehouse inventory not found"));
    }

    private void validateWarehouse(WarehouseInventoryDTO dto) {
        if (warehouseInventoryRepository.existsByWarehouseName(dto.getWarehouseName())) {
            throw new RuntimeException("Warehouse name already exists");
        }
        if (!productRepository.existsById(dto.getProductId())) {
            throw new RuntimeException("Product not found");
        }
        if (dto.getAvailableQuantity() > dto.getMaxCapacity()) {
            throw new RuntimeException("Available quantity cannot exceed max capacity");
        }
    }

    private void validateWarehouseForUpdate(WarehouseInventory warehouse, WarehouseInventoryDTO dto) {
        if (!warehouse.getWarehouseName().equals(dto.getWarehouseName()) &&
                warehouseInventoryRepository.existsByWarehouseName(dto.getWarehouseName())) {
            throw new RuntimeException("Warehouse name already exists");
        }
        if (dto.getAvailableQuantity() > dto.getMaxCapacity()) {
            throw new RuntimeException("Available quantity cannot exceed max capacity");
        }
    }

    private void validateStockUpdate(WarehouseInventory warehouse, Integer quantity) {
        int newQuantity = warehouse.getAvailableQuantity() + quantity;
        if (newQuantity < 0) {
            throw new RuntimeException("Insufficient stock");
        }
        if (newQuantity > warehouse.getMaxCapacity()) {
            throw new RuntimeException("Stock update would exceed max capacity");
        }
    }

    private WarehouseInventory convertToEntity(WarehouseInventoryDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        WarehouseInventory warehouse = new WarehouseInventory();
        warehouse.setWarehouseId(dto.getWarehouseId());
        warehouse.setWarehouseName(dto.getWarehouseName());
        warehouse.setLocation(dto.getLocation());
        warehouse.setProduct(product);
        warehouse.setAvailableQuantity(dto.getAvailableQuantity());
        warehouse.setMaxCapacity(dto.getMaxCapacity());
        warehouse.setWarehouseStatus(dto.getWarehouseStatus());
        return warehouse;
    }

    private void updateWarehouseFields(WarehouseInventory warehouse, WarehouseInventoryDTO dto) {
        Product product = productRepository.findById(dto.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found"));

        warehouse.setWarehouseName(dto.getWarehouseName());
        warehouse.setLocation(dto.getLocation());
        warehouse.setProduct(product);
        warehouse.setAvailableQuantity(dto.getAvailableQuantity());
        warehouse.setMaxCapacity(dto.getMaxCapacity());
        warehouse.setWarehouseStatus(dto.getWarehouseStatus());
    }

    private WarehouseInventoryResponseDTO convertToResponseDTO(WarehouseInventory warehouse) {
        double stockPercentage = (warehouse.getAvailableQuantity() * 100.0) / warehouse.getMaxCapacity();
        boolean lowStockAlert = stockPercentage <= LOW_STOCK_PERCENTAGE;

        return new WarehouseInventoryResponseDTO(
                warehouse.getWarehouseId(),
                warehouse.getWarehouseName(),
                warehouse.getLocation(),
                convertToProductDTO(warehouse.getProduct()),
                warehouse.getAvailableQuantity(),
                warehouse.getMaxCapacity(),
                warehouse.getWarehouseStatus(),
                lowStockAlert,
                (int) stockPercentage
        );
    }

    private ProductResponseDTO convertToProductDTO(Product product) {
        return new ProductResponseDTO(
                product.getProductId(),
                product.getProductSerial(),
                convertToProductCategoryDTO(product.getCategory()),
                product.getStock(),
                product.getPrice(),
                product.getIsDefective(),
                product.getWarrantyPeriod(),
                null, // imageUrl
                product.getStock() < LOW_STOCK_THRESHOLD
        );
    }

    private ProductCategoryResponseDTO convertToProductCategoryDTO(ProductCategory category) {
        return new ProductCategoryResponseDTO(
                category.getCategoryId(),
                category.getCategoryName(),
                category.getBrand(),
                category.getModel(),
                category.getMeasurement(),
                category.getToolType(),
                category.getSpecifications(),
                category.getSparePartType(),
                category.getCompatibility()
        );
    }
}