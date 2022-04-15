package com.silenteight.serp.governance.model;

public class NonResolvableResourceException extends RuntimeException {

  private static final long serialVersionUID = -2306754127917202565L;

  public NonResolvableResourceException(String message) {
    super(message);
  }

  public NonResolvableResourceException() {
  }
}
