package com.silenteight.hsbc.bridge.rest.model.input;

import com.fasterxml.jackson.annotation.JsonValue;
import org.springframework.validation.annotation.Validated;
import javax.validation.Valid;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Gets or Sets BulkStatus
 */
public enum BulkStatus {
  PROCESSING("PROCESSING"),
    ERROR("ERROR"),
    COMPLETED("COMPLETED"),
    CANCEL("CANCEL");

  private String value;

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
