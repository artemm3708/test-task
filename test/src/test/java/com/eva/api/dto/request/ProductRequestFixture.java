package com.eva.api.dto.request;

public class ProductRequestFixture {

  public static ProductRequest createProductRequest() {

    return ProductRequest.builder()
        .name("product")
        .description("description")
        .build();
  }

}
