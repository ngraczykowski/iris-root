package com.silenteight.serp.governance.policy.domain;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Condition {
  IS("is"),
  IS_NOT("is_not");

  String value;

  Condition(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }
}
