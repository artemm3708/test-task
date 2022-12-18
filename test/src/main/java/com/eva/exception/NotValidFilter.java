package com.eva.exception;

import static lombok.AccessLevel.PRIVATE;

import lombok.experimental.FieldDefaults;

@FieldDefaults(level = PRIVATE)
public class NotValidFilter extends RuntimeException{

  public NotValidFilter(String filter) {
    super(filter + " filter is not valid");
  }
}
