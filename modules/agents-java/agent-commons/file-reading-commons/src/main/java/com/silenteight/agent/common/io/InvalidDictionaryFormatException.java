package com.silenteight.agent.common.io;

class InvalidDictionaryFormatException extends RuntimeException {

  private static final long serialVersionUID = -2835550411344514419L;

  InvalidDictionaryFormatException(String line) {
    super(String.format("Line {1%s} has invalid format", line));
  }
}
