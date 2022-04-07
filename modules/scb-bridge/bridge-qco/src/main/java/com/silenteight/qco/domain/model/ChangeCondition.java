package com.silenteight.qco.domain.model;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public record ChangeCondition(String policyId, String stepId, String solution) {

  public static final ChangeCondition NO_CONDITION_FULFILLED =
      new ChangeCondition(EMPTY, EMPTY, EMPTY);
}