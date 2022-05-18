package com.silenteight.payments.bridge.common.exception;

public class MatchNotFoundException extends RuntimeException {

  private static final long serialVersionUID = 6430179609958534030L;

  public MatchNotFoundException(String message) {
    super(message);
  }
}
