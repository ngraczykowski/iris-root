package com.silenteight.hsbc.bridge.alert;

public class AlertConversionException extends RuntimeException {

  private static final long serialVersionUID = -245900496667698478L;

  public AlertConversionException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
