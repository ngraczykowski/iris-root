/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.domain.model;

import static org.apache.commons.lang3.StringUtils.EMPTY;

public record ChangeCondition(String policyId, String stepId, String solution) {

  public static final ChangeCondition NO_CONDITION_FULFILLED =
      new ChangeCondition(EMPTY, EMPTY, EMPTY);
}
