package com.demo.ecommerce.repository;

import com.demo.ecommerce.model.Orders;
import com.demo.ecommerce.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
  Optional<List<Orders>> findByUserAndCreatedBetween(Users user, Date from, Date to);
}
