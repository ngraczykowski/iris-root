package com.silenteight.connector.ftcc.callback.exception;

public class RecoverableCallbackException extends CallbackException {

  private static final long serialVersionUID = -1000164309690182347L;

  public RecoverableCallbackException(Exception exception) {
    super(exception);
  }
}
