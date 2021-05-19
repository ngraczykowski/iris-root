package com.silenteight.serp.governance.agent.domain.file.details;

public class NonResolvableResourceException extends RuntimeException {

  private static final long serialVersionUID = -2306754127917202565L;

  public NonResolvableResourceException(String message) {
    super(message);
  }

  public NonResolvableResourceException() {
  }
}
