package com.example.TransactionManagementSystem;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transactionentity, Long> {

    
    Page<Transactionentity> findByProductTitleContainingOrProductDescriptionContainingOrPrice(
            String title, String description, Double price, Pageable pageable);

   
    @Query("SELECT SUM(t.price) FROM Transactionentity t WHERE t.isSold = true AND MONTH(t.dateOfSale) = :month")
    Double getTotalSaleAmount(@Param("month") int month);


    @Query("SELECT COUNT(t) FROM Transactionentity t WHERE t.isSold = true AND MONTH(t.dateOfSale) = :month")
    Long getTotalSoldItems(@Param("month") int month);

    
    @Query("SELECT COUNT(t) FROM Transactionentity t WHERE t.isSold = false AND MONTH(t.dateOfSale) = :month")
    Long getTotalUnsoldItems(@Param("month") int month);

    
    @Query("SELECT t.price / 100 AS rangeBucket, COUNT(t) FROM Transactionentity t WHERE MONTH(t.dateOfSale) = :month GROUP BY rangeBucket")
    List<Object[]> getPriceRangeCounts(@Param("month") int month);

    
    @Query("SELECT t.category, COUNT(t) FROM Transactionentity t WHERE MONTH(t.dateOfSale) = :month GROUP BY t.category")
    List<Object[]> getCategoryCounts(@Param("month") int month);

   
    @Query("SELECT CASE " +
            "WHEN t.price BETWEEN 0 AND 100 THEN 0 " +
            "WHEN t.price BETWEEN 101 AND 200 THEN 1 " +
            "WHEN t.price BETWEEN 201 AND 300 THEN 2 " +
            "WHEN t.price BETWEEN 301 AND 400 THEN 3 " +
            "WHEN t.price BETWEEN 401 AND 500 THEN 4 " +
            "WHEN t.price BETWEEN 501 AND 600 THEN 5 " +
            "WHEN t.price BETWEEN 601 AND 700 THEN 6 " +
            "WHEN t.price BETWEEN 701 AND 800 THEN 7 " +
            "WHEN t.price BETWEEN 801 AND 900 THEN 8 " +
            "ELSE 9 END AS priceRange, " +
            "COUNT(t) AS itemCount " +
            "FROM Transactionentity t " +
            "WHERE MONTH(t.dateOfSale) = :month " +
            "GROUP BY priceRange")
    List<Object[]> getPriceRangeCounts1(@Param("month") int month);

    
    @Query("SELECT t FROM Transactionentity t WHERE MONTH(t.dateOfSale) = :month")
    Page<Transactionentity> getAllTransactionsByMonth(@Param("month") int month, Pageable pageable);

   
    @Query("SELECT COUNT(t) FROM Transactionentity t WHERE MONTH(t.dateOfSale) = :month")
    Long getTotalTransactions(@Param("month") int month);

   
    @Query("SELECT SUM(t.price) FROM Transactionentity t WHERE MONTH(t.dateOfSale) = :month")
    Double getTotalRevenueForMonth(@Param("month") int month);

   
    @Query("SELECT SUM(t.price) FROM Transactionentity t WHERE t.isSold = true AND MONTH(t.dateOfSale) = :month")
    Double getTotalRevenueFromSoldItems(@Param("month") int month);
}
