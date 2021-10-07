package com.silenteight.payments.bridge.firco.callback.model;

public class NonRecoverableCallbackException extends CallbackException {

  private static final long serialVersionUID = 4268030905590021052L;

  public NonRecoverableCallbackException(Exception exception) {
    super(exception);
  }

}
