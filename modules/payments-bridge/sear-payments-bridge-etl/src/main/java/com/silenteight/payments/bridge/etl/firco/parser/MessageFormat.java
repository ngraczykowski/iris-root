package com.silenteight.payments.bridge.etl.firco.parser;

public enum MessageFormat {
  UNSPECIFIED("UNSPECIFIED"),
  ALL("ALL"),
  SWIFT("SWIFT"),
  FED("FED"),
  IATO("IAT-O"),
  IATI("IAT-I"),
  INT("INT");

  private String messageFormat;

  MessageFormat(String messageFormat) {
    this.messageFormat = messageFormat;
  }

  public String getMessageFormat() {
    return this.messageFormat;
  }
}
