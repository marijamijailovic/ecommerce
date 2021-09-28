package com.demo.ecommerce.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrdersResponseBody {
  private String email;
  private Date orderCreated;
  private ProductResponse products;
  private BigDecimal totalSum;
}
