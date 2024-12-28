package com.maycollins.LlantasApi.repository;

import com.maycollins.LlantasApi.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {
}