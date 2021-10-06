package com.silenteight.payments.bridge.common.exception;

public class NonRecoverableOperationException extends RuntimeException {

  private static final long serialVersionUID = 4268030905590021052L;

  public NonRecoverableOperationException(Exception exception) {
    super(exception);
  }

}
