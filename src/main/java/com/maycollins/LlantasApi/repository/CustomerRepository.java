package com.maycollins.LlantasApi.repository;

import com.maycollins.LlantasApi.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    boolean existsByEmail(String email);

    List<Customer> findByCustomerCategory(String category);

    List<Customer> findByCustomerStatus(String status);

    @Query("SELECT c FROM Customer c WHERE c.customerCategory = :category AND c.customerStatus = :status")
    List<Customer> findByCategoryAndStatus(@Param("category") String category, @Param("status") String status);

    @Query("SELECT COALESCE(SUM(s.total), 0) FROM Sale s WHERE s.customer.customerId = :customerId")
    BigDecimal calculateTotalPurchases(@Param("customerId") Integer customerId);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.customer.customerId = :customerId")
    Integer countTransactions(@Param("customerId") Integer customerId);
}