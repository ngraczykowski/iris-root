package com.silenteight.hsbc.bridge.json;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JsonConversionException extends RuntimeException {

  private static final long serialVersionUID = 2587038986777201805L;

  JsonConversionException(String message) {
    super(message);
  }

  JsonConversionException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
