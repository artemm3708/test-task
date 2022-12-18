package com.eva.repository;

import static org.hibernate.annotations.QueryHints.READ_ONLY;
import static org.hibernate.jpa.HibernateHints.HINT_CACHEABLE;
import static org.hibernate.jpa.HibernateHints.HINT_FETCH_SIZE;

import com.eva.domain.Product;
import jakarta.persistence.QueryHint;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CrudRepository<Product, Long> {

  @Query(value = "select * from products", nativeQuery = true)
  List<Product> findAll();

  @QueryHints(value = {
      @QueryHint(name = HINT_FETCH_SIZE, value = "50"),
      @QueryHint(name = HINT_CACHEABLE, value = "false"),
      @QueryHint(name = READ_ONLY, value = "true")
  })
  @Query(value = "select * from products", nativeQuery = true)
  Stream<Product> getAllProducts();
}
