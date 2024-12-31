package com.maycollins.LlantasApi.repository;

import com.maycollins.LlantasApi.model.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SaleRepository extends JpaRepository<Sale, Integer> {
    List<Sale> findByCustomerCustomerId(Integer customerId);

    List<Sale> findByProductProductId(Integer productId);

    List<Sale> findByUserAccountUserId(Integer userId);

    List<Sale> findBySaleDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT s FROM Sale s WHERE s.customer.customerId = :customerId ORDER BY s.saleDate DESC")
    List<Sale> findRecentSalesByCustomer(@Param("customerId") Integer customerId);

    @Query("SELECT SUM(s.total) FROM Sale s WHERE s.customer.customerId = :customerId")
    BigDecimal calculateTotalPurchasesByCustomer(@Param("customerId") Integer customerId);

    @Query("SELECT COUNT(s) FROM Sale s WHERE s.customer.customerId = :customerId")
    Long countSalesByCustomer(@Param("customerId") Integer customerId);

    @Query("SELECT SUM(s.total) FROM Sale s WHERE DATE(s.saleDate) = CURRENT_DATE")
    BigDecimal calculateDailySales();

    @Query("SELECT COUNT(s) FROM Sale s WHERE DATE(s.saleDate) = CURRENT_DATE")
    Long countDailySales();

    @Query("SELECT s FROM Sale s WHERE s.total >= :amount")
    List<Sale> findHighValueSales(@Param("amount") BigDecimal amount);

    @Query("SELECT s FROM Sale s WHERE s.paymentMethod = :method")
    List<Sale> findByPaymentMethod(@Param("method") String method);
}