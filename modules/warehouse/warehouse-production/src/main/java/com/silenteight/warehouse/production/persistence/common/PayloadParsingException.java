package com.silenteight.warehouse.production.persistence.common;

import com.fasterxml.jackson.core.JsonProcessingException;

class PayloadParsingException extends RuntimeException {

  private static final long serialVersionUID = -5764287714270663711L;

  public PayloadParsingException(JsonProcessingException e) {
    super("Cannot parse payload to string json", e);
  }
}
