package com.silenteight.serp.governance.policy.domain;

import com.fasterxml.jackson.annotation.JsonValue;

import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public enum Condition {

  IS("is"),
  IS_NOT("is_not");

  private final String value;

  Condition(String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }
}
