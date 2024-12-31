package com.maycollins.LlantasApi.controller;

import com.maycollins.LlantasApi.DTO.ProductDTO;
import com.maycollins.LlantasApi.DTO.ProductResponseDTO;
import com.maycollins.LlantasApi.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductDTO dto) {
        return ResponseEntity.ok(productService.createProduct(dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable Integer id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponseDTO>> getProductsByCategory(
            @PathVariable Integer categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }

    @GetMapping("/defective")
    public ResponseEntity<List<ProductResponseDTO>> getDefectiveProducts() {
        return ResponseEntity.ok(productService.getDefectiveProducts());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable Integer id, @RequestBody ProductDTO dto) {
        return ResponseEntity.ok(productService.updateProduct(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }
}