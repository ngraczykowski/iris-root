package com.silenteight.payments.bridge.svb.learning.step.composite.exception;

public class CompositeReadingException extends RuntimeException {

  private static final long serialVersionUID = -7464047854120357225L;

  public CompositeReadingException(Exception e) {
    super(e);
  }
}
