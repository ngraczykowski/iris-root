package com.silenteight.connector.ftcc.callback.exception;

public class NonRecoverableCallbackException extends CallbackException {

  private static final long serialVersionUID = -4610459034493225747L;

  public NonRecoverableCallbackException(Exception exception) {
    super(exception);
  }
}
