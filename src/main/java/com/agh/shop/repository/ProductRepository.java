package com.agh.shop.repository;

import com.agh.shop.jpa.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
/*
    List<Product> findByCategory(String Category);
    //WHERE category = ?

    List<Product> findByNameContainingIgnoreCase(String name);
    //WHERE LOWER(name) LIKE '%?%'

    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    //WHERE price BETWEEN ? AND ?

    List<Product> findByStockQuantityGreaterThan(Integer minStock);
    //WHERE stock_quantity > ?

    List<Product> findByCategoryAndStockQuantityGreaterThan(String category, Integer minStock);
    //WHERE category = ? AND stock_quantity > ?

 */
}
