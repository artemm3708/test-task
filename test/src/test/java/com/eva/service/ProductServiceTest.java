package com.eva.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.eva.api.dto.request.ProductRequest;
import com.eva.api.dto.request.ProductRequestFixture;
import com.eva.domain.Product;
import com.eva.domain.ProductFixture;
import com.eva.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PACKAGE)
public class ProductServiceTest {

  @Mock
  ProductRepository repository;

  @InjectMocks
  ProductServiceImpl service;

  @Captor
  ArgumentCaptor<Product> productArgumentCaptor;

  @Captor
  ArgumentCaptor<Long> productIdCaptor;

  @Captor
  ArgumentCaptor<String> nameCaptor;

  @Captor
  ArgumentCaptor<String> descriptionCaptor;

  @Test
  void findAll() {
    List<Product> products = List.of(ProductFixture.createProduct("name"), ProductFixture.createProduct("new"));
    when(repository.findAll()).thenReturn(products);
    List<Product> actualProducts = service.findAll();
    assertThat(actualProducts.size()).isEqualTo(products.size());
    assertThat(actualProducts).isEqualTo(products);
  }

  @Test
  void findById() {
    Product product = ProductFixture.createProduct("name");
    when(repository.findById(anyLong())).thenReturn(Optional.ofNullable(product));
    service.findById(product.getId());
    verify(repository).findById(productIdCaptor.capture());
    Long id = productIdCaptor.getValue();
    assertThat(product.getId()).isEqualTo(id);
  }

  @Test
  void create() {
    ProductRequest request = ProductRequestFixture.createProductRequest();
    service.addProduct(request);
    verify(repository).save(productArgumentCaptor.capture());
    Product actualProduct = productArgumentCaptor.getValue();
    assertThatProductMappedCorrectly(actualProduct, request);
  }

  private void assertThatProductMappedCorrectly(Product actualProduct, ProductRequest request) {
    assertThat(actualProduct.getName()).isEqualTo(request.getName());
    assertThat(actualProduct.getDescription()).isEqualTo(request.getDescription());
  }

}
