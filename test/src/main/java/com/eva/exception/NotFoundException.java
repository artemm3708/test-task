package com.eva.exception;

import static lombok.AccessLevel.PRIVATE;

import lombok.experimental.FieldDefaults;

@FieldDefaults(level = PRIVATE)
public class NotFoundException extends RuntimeException{

  public NotFoundException(Long id) {
    super("product with id: " + id + " is not found");
  }

}
