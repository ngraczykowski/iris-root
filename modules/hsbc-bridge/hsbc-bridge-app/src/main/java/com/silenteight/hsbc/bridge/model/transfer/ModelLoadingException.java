package com.silenteight.hsbc.bridge.model.transfer;

class ModelLoadingException extends RuntimeException {

  private static final long serialVersionUID = 3781249886849374255L;

  public ModelLoadingException(String message, Throwable cause) {
    super(message, cause);
  }
}
