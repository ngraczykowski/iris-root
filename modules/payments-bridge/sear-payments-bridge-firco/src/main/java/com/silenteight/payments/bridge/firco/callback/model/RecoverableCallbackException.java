package com.silenteight.payments.bridge.firco.callback.model;

public class RecoverableCallbackException extends CallbackException {

  private static final long serialVersionUID = -1031172935564725862L;

  public RecoverableCallbackException(Exception exception) {
    super(exception);
  }

}
