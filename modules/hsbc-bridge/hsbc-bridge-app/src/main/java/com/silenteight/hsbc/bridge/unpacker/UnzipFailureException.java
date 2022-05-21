package com.silenteight.hsbc.bridge.unpacker;

class UnzipFailureException extends RuntimeException {

  private static final long serialVersionUID = -7827966710154273854L;

  UnzipFailureException(Throwable cause) {
    super(cause);
  }
}
