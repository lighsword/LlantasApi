package com.maycollins.LlantasApi.service;

import com.maycollins.LlantasApi.DTO.ProductCategoryResponseDTO;
import com.maycollins.LlantasApi.DTO.ProductDTO;
import com.maycollins.LlantasApi.DTO.ProductResponseDTO;
import com.maycollins.LlantasApi.model.Product;
import com.maycollins.LlantasApi.model.ProductCategory;
import com.maycollins.LlantasApi.repository.ProductCategoryRepository;
import com.maycollins.LlantasApi.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductCategoryRepository categoryRepository;
    private final Integer STOCK_ALERT_THRESHOLD = 10; // Umbral para alertas de stock bajo

    public ProductService(ProductRepository productRepository,
                          ProductCategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }

    public ProductResponseDTO createProduct(ProductDTO productDTO) {
        validateProductSerial(productDTO.getProductSerial());
        Product product = convertToEntity(productDTO);
        Product savedProduct = productRepository.save(product);
        return convertToResponseDTO(savedProduct);
    }

    public ProductResponseDTO getProductById(Integer id) {
        Product product = findProductById(id);
        return convertToResponseDTO(product);
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public ProductResponseDTO updateProduct(Integer id, ProductDTO productDTO) {
        Product product = findProductById(id);
        validateProductSerialForUpdate(product, productDTO.getProductSerial());
        updateProductFields(product, productDTO);
        Product updatedProduct = productRepository.save(product);
        return convertToResponseDTO(updatedProduct);
    }

    public void deleteProduct(Integer id) {
        if (!productRepository.existsById(id)) {
            throw new RuntimeException("Product not found");
        }
        productRepository.deleteById(id);
    }

    public void updateStock(Integer productId, Integer quantity) {
        Product product = findProductById(productId);
        product.setStock(product.getStock() + quantity);
        productRepository.save(product);
    }

    public List<ProductResponseDTO> getLowStockProducts() {
        return productRepository.findByStockLessThan(STOCK_ALERT_THRESHOLD)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductResponseDTO> getProductsByCategory(Integer categoryId) {
        return productRepository.findByCategoryCategoryId(categoryId)
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    private Product findProductById(Integer id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
    }

    private void validateProductSerial(String productSerial) {
        if (productRepository.existsByProductSerial(productSerial)) {
            throw new RuntimeException("Product serial already exists");
        }
    }

    private void validateProductSerialForUpdate(Product product, String newSerial) {
        if (!product.getProductSerial().equals(newSerial) &&
                productRepository.existsByProductSerial(newSerial)) {
            throw new RuntimeException("Product serial already exists");
        }
    }

    private Product convertToEntity(ProductDTO dto) {
        ProductCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Product product = new Product();
        product.setProductId(dto.getProductId());
        product.setProductSerial(dto.getProductSerial());
        product.setCategory(category);
        product.setStock(dto.getStock());
        product.setPrice(dto.getPrice());
        product.setIsDefective(dto.getIsDefective());
        product.setWarrantyPeriod(dto.getWarrantyPeriod());
        return product;
    }

    private void updateProductFields(Product product, ProductDTO dto) {
        ProductCategory category = categoryRepository.findById(dto.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        product.setProductSerial(dto.getProductSerial());
        product.setCategory(category);
        product.setStock(dto.getStock());
        product.setPrice(dto.getPrice());
        product.setIsDefective(dto.getIsDefective());
        product.setWarrantyPeriod(dto.getWarrantyPeriod());
    }

    private ProductResponseDTO convertToResponseDTO(Product product) {
        return new ProductResponseDTO(
                product.getProductId(),
                product.getProductSerial(),
                convertToCategoryDTO(product.getCategory()),
                product.getStock(),
                product.getPrice(),
                product.getIsDefective(),
                product.getWarrantyPeriod(),
                null, // imageUrl - implementar manejo de imágenes
                product.getStock() < STOCK_ALERT_THRESHOLD
        );
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