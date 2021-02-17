package com.silenteight.hsbc.bridge.rest.model.input;

import com.fasterxml.jackson.annotation.JsonValue;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets BulkAlertStatus
 */
public enum BulkAlertStatus {
  STORED("STORED"),
    ERROR("ERROR"),
    PROCESSING("PROCESSING"),
    COMPLETED("COMPLETED");

  private final String value;

  BulkAlertStatus(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static BulkAlertStatus fromValue(String text) {
    for (BulkAlertStatus b : BulkAlertStatus.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
