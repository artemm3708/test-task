package com.eva.api.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.eva.api.dto.response.ErrorResponse;
import com.eva.api.dto.response.ProductResponse;
import com.eva.domain.Product;
import com.eva.domain.ProductFixture;
import com.eva.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = ProductController.class)
@AutoConfigureMockMvc
public class ProductControllerTest {

  public static final String URL = "/shop";

  @Autowired
  MockMvc mockMvc;

  @Autowired
  ObjectMapper objectMapper;

  @MockBean
  ProductService productService;

  @Test
  void getAll_ReturnsProducts() throws Exception {
    Product product = ProductFixture.createProduct("product");
    ProductResponse response = ProductResponse.fromEntity(product);
    List<ProductResponse> responses = List.of(response);
    when(productService.findAll()).thenReturn(List.of(product));
    this.mockMvc.perform((get(URL + "/products")))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().string(objectMapper.writeValueAsString(responses)));
  }

  @Test
  void get_ValidProductId_ReturnsProduct() throws Exception {
    Product product = ProductFixture.createProduct("product");
    ProductResponse response = ProductResponse.fromEntity(product);
    long id = 1L;
    when(productService.findById(eq(id))).thenReturn(Optional.of(product));
    this.mockMvc.perform((get(URL + "/" + id)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().string(objectMapper.writeValueAsString(response)));
  }

  @Test
  void get_InvalidProductId_ReturnsError() throws Exception {
    long id = 1L;
    when(productService.findById(eq(id))).thenReturn(Optional.empty());
    ErrorResponse errorResponse = new ErrorResponse(404, 404, "product with id: 1 is not found");
    this.mockMvc.perform((get(URL + "/" + id)))
        .andExpect(status().is4xxClientError())
        .andExpect(content().string(objectMapper.writeValueAsString(errorResponse)));
  }

  @Test
  void getAllByFilter_ReturnsProducts() throws Exception {
    Product product = ProductFixture.createProduct("product");
    Product product1 = ProductFixture.createProduct("roduct");
    ProductResponse response1 = ProductResponse.fromEntity(product1);
    List<ProductResponse> responses = List.of(response1);
    String filter = "%5EP.*%24";
    when(productService.findAll()).thenReturn(List.of(product, product1));
    this.mockMvc.perform((get(URL + "/product?filter=" + filter)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().string(objectMapper.writeValueAsString(responses)));
  }

  @Test
  void getAllByFilter_ReturnsProducts2() throws Exception {
    Product product = ProductFixture.createProduct("product");
    Product product1 = ProductFixture.createProduct("roduct");
    ProductResponse response1 = ProductResponse.fromEntity(product1);
    List<ProductResponse> responses = List.of(response1);
    String filter = "%5EP.*%24";
    when(productService.findAll()).thenReturn(List.of(product, product1));
    this.mockMvc.perform((get(URL + "/product?filter=" + filter)))
        .andExpect(status().is2xxSuccessful())
        .andExpect(content().string(objectMapper.writeValueAsString(responses)));
  }

}
