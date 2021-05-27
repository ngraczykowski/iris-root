package com.silenteight.adjudication.engine.common.integration;

public class NoCorrelationIdException extends RuntimeException {

  private static final long serialVersionUID = -2509006362983311847L;

  public NoCorrelationIdException() {
    super("No correlation ID received");
  }
}
