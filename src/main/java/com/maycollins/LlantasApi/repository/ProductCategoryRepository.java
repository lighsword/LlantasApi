package com.maycollins.LlantasApi.repository;

import com.maycollins.LlantasApi.model.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {
    boolean existsByCategoryName(String categoryName);

    // Método para buscar por las categorías deseadas
    @Query("SELECT pc FROM ProductCategory pc WHERE pc.categoryName IN (:categories)")
    List<ProductCategory> findByCategoryNameIn(@Param("categories") List<String> categories);
}
