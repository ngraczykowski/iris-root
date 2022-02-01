package com.silenteight.payments.bridge.svb.learning.reader.domain.exception;

public final class NoCorrespondingAlertException extends RuntimeException {

  private static final long serialVersionUID = -6165977099178529528L;

  public NoCorrespondingAlertException(String message) {
    super(message);
  }
}
