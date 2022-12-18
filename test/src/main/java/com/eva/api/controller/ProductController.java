package com.eva.api.controller;

import static lombok.AccessLevel.PRIVATE;

import com.eva.api.dto.request.ProductRequest;
import com.eva.api.dto.response.ProductResponse;
import com.eva.domain.Product;
import com.eva.exception.NotFoundException;
import com.eva.service.ProductService;
import com.google.gson.Gson;
import jakarta.validation.Valid;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@Controller
@Slf4j
@RestController
@RequestMapping("/shop")
@RequiredArgsConstructor
@FieldDefaults(level = PRIVATE, makeFinal = true)
public class ProductController {

  ProductService productService;
  Gson gson;

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
      String regex = decode(filter);
    log.info("request received to fetch all product details");
    List<ProductResponse> products = productService.findAll().stream()
        .filter(product -> !(Pattern.matches(regex.toLowerCase(), product.getName().toLowerCase())))
        .map(ProductResponse::fromEntity)
        .toList();
    log.info("finished streaming records");
    return products;
  }

  @GetMapping(value = "/product/streams")
  public ResponseEntity<StreamingResponseBody> findAllProducts(@Valid @RequestParam String filter) {

    String regex = decode(filter);
    log.info("request received to fetch all product details");
    Stream<ProductResponse> products = productService.findProductsByFilter()
      .filter(product -> !(Pattern.matches(regex, product.getName())))
      .map(ProductResponse::fromEntity);
    StreamingResponseBody responseBody = httpResponseOutputStream -> {
    try (Writer writer = new BufferedWriter(new OutputStreamWriter(httpResponseOutputStream))) {
      products.forEach(product -> {
        try {
          writer.write(gson.toJson(product));
          writer.flush();
        } catch (IOException exception) {
          log.error("exception occurred while writing object to stream", exception);
        }
      });
    } catch (Exception exception) {
      log.error("exception occurred while publishing data", exception);
    }
    log.info("finished streaming records");
  };
    return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.APPLICATION_JSON).body(responseBody);
}

  private String decode(String value) {
    return URLDecoder.decode(value, StandardCharsets.UTF_8);
  }

}
