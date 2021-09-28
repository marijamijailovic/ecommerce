package com.demo.ecommerce.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class Users {

  @Id
  @GenericGenerator(name = "uuid2", strategy = "uuid2")
  @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
  private UUID id;

  @Temporal(TemporalType.TIMESTAMP)
  private Date created;

  private String email;
}
