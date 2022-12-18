package com.eva.domain;

import static lombok.AccessLevel.PRIVATE;

import com.eva.api.dto.request.ProductRequest;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "products")
@FieldDefaults(level = PRIVATE)
@Builder
public class Product {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_seq")
  @SequenceGenerator(name = "products_seq", sequenceName = "products_seq", allocationSize = 1)
  private Long id;

  @Column
  String name;

  @Column
  String description;

  public static Product create(ProductRequest request) {
    return Product.builder()
        .name(request.getName())
        .description(request.getDescription())
        .build();
  }

}
