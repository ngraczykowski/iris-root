package com.silenteight.payments.bridge.svb.oldetl.model;

/**
 * Thrown to indicate that a message is invalid, thus it cannot be processed to the best of our
 * knowledge.
 */
public class UnsupportedMessageException extends RuntimeException {

  private static final long serialVersionUID = -6400295849618842182L;

  public UnsupportedMessageException(String s) {
    super(s);
  }

  public UnsupportedMessageException(String message, Throwable cause) {
    super(message, cause);
  }
}
