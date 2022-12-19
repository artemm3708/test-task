package com.eva.api.controller;

import static lombok.AccessLevel.PRIVATE;

import com.eva.api.dto.request.ProductRequest;
import com.eva.api.dto.response.ProductResponse;
import com.eva.domain.Product;
import com.eva.exception.NotFoundException;
import com.eva.exception.NotValidFilter;
import com.eva.repository.ProductRepository;
import com.eva.service.ProductService;
import jakarta.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@Slf4j
@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProductController {

  ProductService productService;

  @GetMapping("/products")
  public List<ProductResponse> getAll() {
    return productService.findAll().stream()
        .map(ProductResponse::fromEntity)
        .toList();
  }

  @GetMapping("/{id}")
  public ProductResponse getById(@PathVariable Long id) {
    return productService.findById(id)
        .map(ProductResponse::fromEntity)
        .orElseThrow(() -> new NotFoundException(id));
  }

  @PostMapping()
  public ProductResponse addProduct(@RequestBody ProductRequest request) {
    Product newProduct = productService.addProduct(request);
    return ProductResponse.fromEntity(newProduct);
  }

  @GetMapping("/product")
  public List<ProductResponse> getAllByFilter(@Valid @RequestParam String filter) {
    log.info("Request received");
      String regex = decode(filter);
    List<ProductResponse> products = productService.findAll().stream()
        .filter(product -> !(Pattern.matches(regex.toLowerCase(), product.getName().toLowerCase())))
        .map(ProductResponse::fromEntity)
        .toList();
    log.info("Finish execution");
      return products;
  }

  @GetMapping("/product/peagable/{page}")
  public List<ProductResponse> getAllPage(@Valid @RequestParam String filter, @PathVariable Long page) {
    log.info("Request received");
      String regex = decode(filter);
      List<ProductResponse> products = productService.findAll().stream()
          .filter(product -> !(Pattern.matches(regex.toLowerCase(), product.getName().toLowerCase())))
          .map(ProductResponse::fromEntity)
          .skip((page -1) * 100)
          .limit(100)
          .toList();
    log.info("Finish execution");
    return products;
  }

  private String decode(String value){
    return URLDecoder.decode(value, StandardCharsets.UTF_8);
  }

}
