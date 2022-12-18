package com.eva.api.dto.response;

import static org.springframework.util.Assert.notNull;

public record ErrorResponse(Integer status, Integer code, String message) {

  public ErrorResponse(Integer status, Integer code, String message) {
    this.status = status;
    notNull(code, "Code cannot be null.");
    notNull(message, "Message cannot be null.");
    this.code = code;
    this.message = message;
  }

}
