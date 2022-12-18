package com.eva.service;


import com.eva.api.dto.request.ProductRequest;
import com.eva.domain.Product;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

public interface ProductService {

  List<Product> findAll();

  Optional<Product> findById(Long id);
  Product addProduct(ProductRequest request);
  Stream<Product> findProductsByFilter();

}
