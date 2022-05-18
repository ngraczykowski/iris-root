package com.silenteight.payments.bridge.etl.parser.service;

public class MessageParsingException extends IllegalArgumentException {

  private static final long serialVersionUID = -1566564007750544018L;

  MessageParsingException(String message, Throwable cause) {
    super(message, cause);
  }
}
