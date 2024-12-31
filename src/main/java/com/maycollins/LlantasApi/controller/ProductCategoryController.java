package com.maycollins.LlantasApi.controller;

import com.maycollins.LlantasApi.DTO.ProductCategoryDTO;
import com.maycollins.LlantasApi.DTO.ProductCategoryResponseDTO;
import com.maycollins.LlantasApi.model.ProductCategory;
import com.maycollins.LlantasApi.service.ProductCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product-categories")
@CrossOrigin(origins = "*")
public class ProductCategoryController {

    private final ProductCategoryService productCategoryService;

    public ProductCategoryController(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @PostMapping
    public ResponseEntity<ProductCategoryResponseDTO> createProductCategory(@RequestBody ProductCategoryDTO dto) {
        return ResponseEntity.ok(productCategoryService.createProductCategory(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategoryResponseDTO> getProductCategoryById(@PathVariable Integer id) {
        return ResponseEntity.ok(productCategoryService.getProductCategoryById(id));
    }

    // Lista de producto en general
    @GetMapping
    public ResponseEntity<List<ProductCategoryResponseDTO>> getAllProductCategories() {
        return ResponseEntity.ok(productCategoryService.getAllProductCategories());
    }

    @GetMapping("/filter")
    public ResponseEntity<List<ProductCategory>> getCategoriesByType(
            @RequestParam List<String> categoryTypes) {
        List<ProductCategory> categories = productCategoryService.getCategoriesByType(categoryTypes);
        return ResponseEntity.ok(categories);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCategoryResponseDTO> updateProductCategory(
            @PathVariable Integer id, @RequestBody ProductCategoryDTO dto) {
        return ResponseEntity.ok(productCategoryService.updateProductCategory(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductCategory(@PathVariable Integer id) {
        productCategoryService.deleteProductCategory(id);
        return ResponseEntity.ok().build();
    }
}
