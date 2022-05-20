package com.silenteight.hsbc.bridge.unpacker;

class DeleteFailureException extends RuntimeException {

  private static final long serialVersionUID = -7827966710154273854L;

  DeleteFailureException(String message, Throwable cause) {
    super(message, cause);
  }
}
