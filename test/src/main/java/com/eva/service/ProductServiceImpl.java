package com.eva.service;

import static lombok.AccessLevel.PRIVATE;

import com.eva.api.dto.request.ProductRequest;
import com.eva.domain.Product;
import com.eva.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService{

  ProductRepository repository;

  @Override
  public List<Product> findAll() {
    return repository.findAll();
  }

  @Override
  public Optional<Product> findById(Long id) {
    return repository.findById(id);
  }

  @Override
  public Product addProduct(ProductRequest request) {
    return save(Product.create(request));
  }

  private Product save(Product product) {
    return repository.save(product);
  }


}
