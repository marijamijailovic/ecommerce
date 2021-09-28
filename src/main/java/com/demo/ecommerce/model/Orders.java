package com.demo.ecommerce.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="orders")
public class Orders {

  @Id
  @Column(length = 36, nullable = false, updatable = false)
  private UUID id;

  @Temporal(TemporalType.TIMESTAMP)
  @Column(name = "created", nullable = false, insertable = false)
  private Date created;

  @ManyToOne
  @JoinColumn(name = "users_id", referencedColumnName = "id", nullable = false)
  @JsonIgnore
  private Users user;

  @OneToMany(mappedBy = "orders")
  @JsonIgnore
  private List<OrderProduct> orderProducts;
}
