package com.maycollins.LlantasApi.repository;

import com.maycollins.LlantasApi.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}