package com.silenteight.payments.bridge.svb.learning.reader.domain.exception;

public final class NoCorrespondingMatchException extends RuntimeException {


  private static final long serialVersionUID = 7589564696078606972L;

  public NoCorrespondingMatchException(String message) {
    super(message);
  }
}
