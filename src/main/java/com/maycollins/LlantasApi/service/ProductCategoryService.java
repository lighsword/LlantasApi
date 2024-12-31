package com.maycollins.LlantasApi.service;

import com.maycollins.LlantasApi.DTO.ProductCategoryDTO;
import com.maycollins.LlantasApi.DTO.ProductCategoryResponseDTO;
import com.maycollins.LlantasApi.model.ProductCategory;
import com.maycollins.LlantasApi.repository.ProductCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductCategoryService {

    private final ProductCategoryRepository productCategoryRepository;

    @Autowired
    public ProductCategoryService(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    public ProductCategoryResponseDTO createProductCategory(ProductCategoryDTO dto) {
        if (productCategoryRepository.existsByCategoryName(String.valueOf(dto.getCategoryName()))) {
            throw new RuntimeException("Category name already exists");
        }
        ProductCategory productCategory = convertToEntity(dto);
        ProductCategory savedCategory = productCategoryRepository.save(productCategory);
        return convertToResponseDTO(savedCategory);
    }

    public ProductCategoryResponseDTO getProductCategoryById(Integer id) {
        ProductCategory productCategory = findById(id);
        return convertToResponseDTO(productCategory);
    }

    // Llarmar la lista de productos

    public List<ProductCategoryResponseDTO> getAllProductCategories() {
        return productCategoryRepository.findAll()
                .stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<ProductCategory> getCategoriesByType(List<String> categories) {
        return productCategoryRepository.findByCategoryNameIn(categories);
    }

    public ProductCategoryResponseDTO updateProductCategory(Integer id, ProductCategoryDTO dto) {
        ProductCategory productCategory = findById(id);
        updateFields(productCategory, dto);
        ProductCategory updatedCategory = productCategoryRepository.save(productCategory);
        return convertToResponseDTO(updatedCategory);
    }

    public void deleteProductCategory(Integer id) {
        if (!productCategoryRepository.existsById(id)) {
            throw new RuntimeException("Product category not found");
        }
        productCategoryRepository.deleteById(id);
    }

    private ProductCategory findById(Integer id) {
        return productCategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product category not found"));
    }

    private void updateFields(ProductCategory productCategory, ProductCategoryDTO dto) {
        productCategory.setCategoryName(dto.getCategoryName());
        productCategory.setBrand(dto.getBrand());
        productCategory.setModel(dto.getModel());
        productCategory.setMeasurement(dto.getMeasurement());
        productCategory.setToolType(dto.getToolType());
        productCategory.setSpecifications(dto.getSpecifications());
        productCategory.setSparePartType(dto.getSparePartType());
        productCategory.setCompatibility(dto.getCompatibility());
    }

    private ProductCategory convertToEntity(ProductCategoryDTO dto) {
        return new ProductCategory(
                dto.getCategoryId(),
                dto.getCategoryName(),
                dto.getBrand(),
                dto.getModel(),
                dto.getMeasurement(),
                dto.getToolType(),
                dto.getSpecifications(),
                dto.getSparePartType(),
                dto.getCompatibility()
        );
    }

    private ProductCategoryResponseDTO convertToResponseDTO(ProductCategory productCategory) {
        return new ProductCategoryResponseDTO(
                productCategory.getCategoryId(),
                productCategory.getCategoryName(),
                productCategory.getBrand(),
                productCategory.getModel(),
                productCategory.getMeasurement(),
                productCategory.getToolType(),
                productCategory.getSpecifications(),
                productCategory.getSparePartType(),
                productCategory.getCompatibility()
        );
    }
}
