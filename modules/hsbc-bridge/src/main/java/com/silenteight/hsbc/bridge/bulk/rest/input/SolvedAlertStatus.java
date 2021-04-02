package com.silenteight.hsbc.bridge.bulk.rest.input;

import com.fasterxml.jackson.annotation.JsonValue;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets SolvedAlertStatus
 */
public enum SolvedAlertStatus {
  MANUAL_INVESTIGATION("MANUAL_INVESTIGATION"),
  POTENTIAL_TRUE_POSITIVE("POTENTIAL_TRUE_POSITIVE"),
  FALSE_POSITIVE("FALSE_POSITIVE");

  private final String value;

  SolvedAlertStatus(String value) {
    this.value = value;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static SolvedAlertStatus fromValue(String text) {
    for (SolvedAlertStatus b : SolvedAlertStatus.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }
}
