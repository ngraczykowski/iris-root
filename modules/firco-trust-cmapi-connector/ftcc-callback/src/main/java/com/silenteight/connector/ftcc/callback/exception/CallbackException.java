package com.silenteight.connector.ftcc.callback.exception;

public class CallbackException extends RuntimeException {

  private static final long serialVersionUID = 2790138345483754988L;

  public CallbackException(Exception exception) {
    super(exception);
  }
}
