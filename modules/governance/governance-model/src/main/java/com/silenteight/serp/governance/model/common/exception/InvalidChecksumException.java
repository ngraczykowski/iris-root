package com.silenteight.serp.governance.model.common.exception;

public class InvalidChecksumException extends RuntimeException {

  private static final long serialVersionUID = 285882998677431044L;

  public InvalidChecksumException(String message) {
    super(message);
  }

  public InvalidChecksumException(String message, Throwable cause) {
    super(message, cause);
  }
}
