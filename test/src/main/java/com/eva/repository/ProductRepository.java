package com.eva.repository;

import com.eva.domain.Product;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

  @Query(value = "select * from products", nativeQuery = true)
  List<Product> findAll();
}
