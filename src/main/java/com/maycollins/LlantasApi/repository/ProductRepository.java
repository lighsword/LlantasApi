package com.maycollins.LlantasApi.repository;

import com.maycollins.LlantasApi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    boolean existsBySerialNumber(String serialNumber); // Cambiado de productSerial a serialNumber
    List<Product> findByCategory_CategoryId(Integer categoryId);
    List<Product> findByIsDefective(Boolean isDefective);
    List<Product> findByStockLessThan(Integer stockMinimo);
}