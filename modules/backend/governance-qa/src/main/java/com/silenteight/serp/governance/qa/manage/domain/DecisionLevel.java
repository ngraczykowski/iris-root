package com.silenteight.serp.governance.qa.manage.domain;

public enum DecisionLevel {

  ANALYSIS(0),
  VALIDATION(1);

  private final int value;

  DecisionLevel(final int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }
}
