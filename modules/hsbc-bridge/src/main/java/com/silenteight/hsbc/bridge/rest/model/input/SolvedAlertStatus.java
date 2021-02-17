package com.silenteight.hsbc.bridge.rest.model.input;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets SolvedAlertStatus
 */
public enum SolvedAlertStatus {
  POTENTIAL_MATCH("POTENTIAL_MATCH"),
    NO_MATCH("NO_MATCH"),
    MATCH("MATCH"),
    FALSE_MATCH("FALSE_MATCH"),
    ERROR("ERROR");

  private String value;

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
