package com.eva.api.controller;

import static lombok.AccessLevel.PRIVATE;

import com.eva.api.dto.request.ProductRequest;
import com.eva.api.dto.response.ProductResponse;
import com.eva.domain.Product;
import com.eva.exception.NotFoundException;
import com.eva.exception.NotValidFilter;
import com.eva.service.ProductService;
import jakarta.validation.Valid;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
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
    try {
      String regex = decode(filter);
      return productService.findAll().stream()
          .filter(product -> !(Pattern.matches(regex.toLowerCase(), product.getName().toLowerCase())))
          .map(ProductResponse::fromEntity)
          .toList();
    } catch (UnsupportedEncodingException e) {
      throw new NotValidFilter(filter);
    }
  }
  private String decode(String value) throws UnsupportedEncodingException {
    return URLDecoder.decode(value, StandardCharsets.UTF_8);
  }

}
