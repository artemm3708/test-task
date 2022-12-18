package com.eva.service;

import static lombok.AccessLevel.PRIVATE;

import com.eva.api.dto.request.ProductRequest;
import com.eva.domain.Product;
import com.eva.repository.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService{

  ProductRepository repository;
  JdbcTemplate jdbcTemplate;

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

  @Override
  @Transactional(readOnly = true)
  public Stream<Product> findProductsByFilter() {
    return jdbcTemplate.queryForStream("Select * from products",
        (resultSet, rowNum) ->
            new Product(resultSet.getLong("id"), resultSet.getString("name"),
                resultSet.getString("description")));
  }

  private Product save(Product product) {
    return repository.save(product);
  }

}
