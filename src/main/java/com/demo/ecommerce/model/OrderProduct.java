package com.demo.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders_products")
public class OrderProduct {

  @EmbeddedId
  @JsonIgnore
  private OrderProductPK orderProductPK;

  @ManyToOne
  @MapsId("ordersId")
  @JoinColumn(name="orders_id")
  private Orders orders;

  @ManyToOne
  @MapsId("productsId")
  @JoinColumn(name="products_id")
  private Products products;

  @Column(name = "products_price", nullable = false)
  private BigDecimal productPrice;
}
