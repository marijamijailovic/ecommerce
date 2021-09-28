package com.demo.ecommerce.repository;

import com.demo.ecommerce.model.OrderProduct;
import com.demo.ecommerce.model.OrderProductPK;
import com.demo.ecommerce.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderProductRepository extends JpaRepository<OrderProduct, OrderProductPK> {
  Optional<List<OrderProduct>> findByOrders(Orders order);

  @Query(value="select sum(products_price) as totalSum from orders_products where orders_id = :orderId", nativeQuery = true)
  Optional<BigDecimal> getTotalOrderSum(@Param("orderId") UUID orderId);
}
