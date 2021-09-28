package com.demo.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="products")
public class Products {

  @Id
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
  private UUID id;

  @Temporal(TemporalType.TIMESTAMP)
  @JsonIgnore
  private Date created;

  private String category;

  private String name;

  private BigDecimal price;

  @Temporal(TemporalType.TIMESTAMP)
  @JsonIgnore
  private Date updated;

  @OneToMany(mappedBy = "products")
  @JsonIgnore
  private List<OrderProduct> orderProducts;

}
