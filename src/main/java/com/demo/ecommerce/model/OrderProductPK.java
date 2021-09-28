package com.demo.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import java.io.Serializable;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class OrderProductPK implements Serializable {

  @JoinColumn(name="orders_id")
  private UUID ordersId;

  @JoinColumn(name="products_id")
  private UUID productsId;
}
