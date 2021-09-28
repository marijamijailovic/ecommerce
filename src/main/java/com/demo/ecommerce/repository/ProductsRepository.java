package com.demo.ecommerce.repository;

import com.demo.ecommerce.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductsRepository extends JpaRepository<Products, Long> {
  @Query(value="SELECT * FROM products p WHERE p.id=:productId", nativeQuery = true)
  Optional<Products> findByUuid(@Param("productId") UUID productId);

  Optional<Products> findByCategoryAndNameAndPrice(String category, String name, BigDecimal price);
}
