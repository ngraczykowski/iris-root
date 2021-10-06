package com.silenteight.payments.bridge.common.exception;

public class RecoverableOperationException extends RuntimeException {

  private static final long serialVersionUID = -1031172935564725862L;

  public RecoverableOperationException(Exception exception) {
    super(exception);
  }

}
