package com.silenteight.serp.governance.policy.domain.exception;

import lombok.NonNull;

public class EmptyMatchConditionValueException extends RuntimeException {

  private static final long serialVersionUID = -9001231432484763512L;

  public EmptyMatchConditionValueException(@NonNull String featureName) {
    super(String.format("Could not create a condition with empty values for feature %s",
                        featureName));
  }
}
