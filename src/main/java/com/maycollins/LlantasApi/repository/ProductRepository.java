package com.maycollins.LlantasApi.repository;

import com.maycollins.LlantasApi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsByProductSerial(String productSerial);
    List<Product> findByStockLessThan(Integer minStock);
    List<Product> findByCategoryCategoryId(Integer categoryId);
    List<Product> findByIsDefective(Boolean isDefective);
}