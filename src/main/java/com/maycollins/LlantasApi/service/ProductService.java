package com.maycollins.LlantasApi.service;

import com.maycollins.LlantasApi.DTO.ProductCategoryResponseDTO;
import com.maycollins.LlantasApi.DTO.ProductDTO;
import com.maycollins.LlantasApi.DTO.ProductResponseDTO;
import com.maycollins.LlantasApi.model.Product;
import com.maycollins.LlantasApi.model.ProductCategory;
import com.maycollins.LlantasApi.repository.ProductRepository;
import com.maycollins.LlantasApi.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductService(ProductRepository productRepository,
                          ProductCategoryRepository productCategoryRepository) {
        this.productRepository = productRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    public ProductResponseDTO createProduct(ProductDTO dto) {
        validateProduct(dto);
        Product product = convertToEntity(dto);
        Product savedProduct = productRepository.save(product);
        return convertToResponseDTO(savedProduct);
    }

    public ProductResponseDTO getProductById(Integer id) {
        Product product = findById(id);
        return convertToResponseDTO(product);
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO updateProduct(Integer id, ProductDTO dto) {
        Product product = findById(id);
        validateSerialForUpdate(product, dto.getSerialNumber());
        updateProductFields(product, dto);
        Product updatedProduct = productRepository.save(product);
        return convertToResponseDTO(updatedProduct);
    }

    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

    public List<ProductResponseDTO> getProductsByCategory(Integer categoryId) {
        return productRepository.findByCategory_CategoryId(categoryId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getDefectiveProducts() {
        return productRepository.findByIsDefective(true)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private Product findById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    private void validateProduct(ProductDTO dto) {
        if (productRepository.existsBySerialNumber(dto.getSerialNumber())) { // Cambiado
            throw new RuntimeException("Product serial number already exists");
        }
    }

    private void validateSerialForUpdate(Product product, String newSerial) {
        if (!product.getSerialNumber().equals(newSerial) && // Cambiado
                productRepository.existsBySerialNumber(newSerial)) { // Cambiado
            throw new RuntimeException("Product serial number already exists");
        }
    }

    private Product convertToEntity(ProductDTO dto) {
        ProductCategory category = productCategoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setProductId(dto.getProductId());
        product.setSerialNumber(dto.getSerialNumber());
        product.setCategory(category);
        product.setPrice(dto.getPrice());
        product.setIsDefective(dto.getIsDefective());
        product.setWarrantyPeriod(dto.getWarrantyPeriod());
        return product;
    }

    private ProductResponseDTO convertToResponseDTO(Product product) {
        ProductResponseDTO dto = new ProductResponseDTO();
        dto.setProductId(product.getProductId());
        dto.setSerialNumber(product.getSerialNumber());
        dto.setCategory(convertToCategoryDTO(product.getCategory()));
        dto.setPrice(product.getPrice());
        dto.setIsDefective(product.getIsDefective());
        dto.setWarrantyPeriod(product.getWarrantyPeriod());
        return dto;
    }

    private void updateProductFields(Product product, ProductDTO dto) {
        if (dto.getCategoryId() != null) {
            ProductCategory category = productCategoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }
        product.setSerialNumber(dto.getSerialNumber());
        product.setPrice(dto.getPrice());
        product.setIsDefective(dto.getIsDefective());
        product.setWarrantyPeriod(dto.getWarrantyPeriod());
    }

    private ProductCategoryResponseDTO convertToCategoryDTO(ProductCategory category) {
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