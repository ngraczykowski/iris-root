package com.silenteight.hsbc.bridge.rest.model.output;

import com.fasterxml.jackson.annotation.JsonValue;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets BulkStatus
 */
public enum BulkStatus {
  STORED("STORED"),
  ERROR("ERROR"),
  CANCELLED("CANCELLED"),
  PROCESSING("PROCESSING"),
  COMPLETED("COMPLETED");

  private final String value;

  BulkStatus(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static BulkStatus fromValue(String text) {
    for (BulkStatus b : BulkStatus.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
