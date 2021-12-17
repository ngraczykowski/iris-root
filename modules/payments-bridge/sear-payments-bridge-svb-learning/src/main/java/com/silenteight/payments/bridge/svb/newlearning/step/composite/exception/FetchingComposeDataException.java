package com.silenteight.payments.bridge.svb.newlearning.step.composite.exception;

public class FetchingComposeDataException extends RuntimeException {

  private static final long serialVersionUID = -4521052341208773650L;

  public FetchingComposeDataException(Exception e) {
    super(e);
  }

  public FetchingComposeDataException(String message, Exception e) {
    super(message, e);
  }
}
