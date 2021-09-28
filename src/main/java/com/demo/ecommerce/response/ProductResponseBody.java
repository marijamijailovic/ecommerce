package com.demo.ecommerce.response;

import com.demo.ecommerce.model.Products;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseBody{
  private UUID id;
  private String category;
  private String name;
  private BigDecimal price;

  public static ProductResponseBody createResponseFromProduct(Products product) {
    if(Objects.isNull(product)) {
      return ProductResponseBody.builder().build();
    }
    return ProductResponseBody.builder()
        .id(product.getId())
        .category(product.getCategory())
        .name(product.getName())
        .price(product.getPrice())
        .build();
  }
}
