/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.ingest.adapter.incomming.gnsrt.model;

import lombok.AllArgsConstructor;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

@AllArgsConstructor
public enum GnsRtAlertStatus {
  POTENTIAL_MATCH("POTENTIAL_MATCH"),
  NO_MATCH("NO_MATCH"),
  MATCH("MATCH"),
  FALSE_MATCH("FALSE_MATCH"),
  ERROR("ERROR");

  private final String value;

  @JsonCreator
  public static GnsRtAlertStatus fromValue(String text) {
    for (GnsRtAlertStatus b : GnsRtAlertStatus.values()) {
      if (String.valueOf(b.value).equals(text)) {
        return b;
      }
    }
    return null;
  }

  @Override
  @JsonValue
  public String toString() {
    return String.valueOf(value);
  }
}
