package com.silenteight.payments.bridge.notification.model;

public class NonRecoverableEmailSendingException extends RuntimeException {

  public NonRecoverableEmailSendingException(String message) {
    super(message);
  }
}
