package com.silenteight.payments.bridge.svb.newlearning.domain;

public class NoCorrespondingHitException extends RuntimeException {

  private static final long serialVersionUID = -2383244233253458660L;

  public NoCorrespondingHitException(String message) {
    super(message);
  }
}
