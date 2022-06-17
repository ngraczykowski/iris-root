package com.silenteight.serp.governance.common.signature;

public class InvalidInputException extends RuntimeException {

  private static final long serialVersionUID = 2316990279869076182L;

  InvalidInputException(String message) {
    super(message);
  }
}
