package com.eva.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eva.domain.Product;
import com.eva.domain.ProductFixture;
import com.eva.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Testcontainers
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductIntegrationTest {

  public static final String PRODUCTS_URL = "/shop";

  @Autowired
  ProductRepository productRepository;

  public static PostgreSQLContainer<?> DB_CONTAINER = new PostgreSQLContainer<>("postgres:14.2-alpine");

  static {
    DB_CONTAINER.start();
  }

  @Autowired
  MockMvc mockMvc;

  @DynamicPropertySource
  public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
    registry.add("spring.datasource.url", DB_CONTAINER::getJdbcUrl);
    registry.add("spring.datasource.username", DB_CONTAINER::getUsername);
    registry.add("spring.datasource.password", DB_CONTAINER::getPassword);
  }

  @BeforeEach
  void setUp(){
    productRepository.deleteAll();
  }

  @Test
  public void givenListOfProducts_whenGetAllProducts_thenReturnProducts() throws Exception{
    List<Product> products = new ArrayList<>();
    products.add(ProductFixture.createProduct("product"));
    productRepository.saveAll(products);

    ResultActions response = mockMvc.perform(get(PRODUCTS_URL + "/products"));

    response.andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.size()", is(products.size())));
  }

  @Test
  public void givenListOfProducts_whenGetByFilter_thenReturnProducts() throws Exception{
    List<Product> products = new ArrayList<>();

    for(long i = 0; i < 10000; i++) {
      products.add(ProductFixture.createProduct("product", i));
    }
    for(long i = 0; i < 10000; i++) {
      products.add(ProductFixture.createProduct("roduct", i));
    }

    productRepository.saveAll(products);
    String filter = "%5EP.*%24";

    ResultActions response = mockMvc.perform(get(PRODUCTS_URL + "/product?filter=" + filter));

    response.andExpect(status().isOk())
        .andDo(print())
        .andExpect(jsonPath("$.size()", is(10000)));
  }

}
