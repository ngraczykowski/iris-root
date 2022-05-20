package com.silenteight.payments.bridge.app.batch;

public final class JobNameNotFoundException extends RuntimeException {

  private static final long serialVersionUID = -831549840505683353L;

  public JobNameNotFoundException(String message) {
    super(message);
  }
}
