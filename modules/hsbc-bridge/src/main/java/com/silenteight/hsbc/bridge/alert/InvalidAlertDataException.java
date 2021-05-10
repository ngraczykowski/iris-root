package com.silenteight.hsbc.bridge.alert;

class InvalidAlertDataException extends RuntimeException {

  private static final long serialVersionUID = -5953573221134485573L;

  InvalidAlertDataException(String message) {
    super(message);
  }
}
