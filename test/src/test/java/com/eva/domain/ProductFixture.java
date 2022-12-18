package com.eva.domain;

public class ProductFixture {

  public static Product createProduct(String name) {
    return Product.builder()
        .id(1L)
        .name(name)
        .description("description")
        .build();
  }

  public static Product createProduct(String name, Long id) {
    return Product.builder()
        .id(id)
        .name(name)
        .description("description")
        .build();
  }
}
