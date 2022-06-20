/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.domain;

import com.silenteight.iris.qco.domain.model.ChangeCondition;

interface ChangeConditionFactory {

  ChangeCondition createChangeCondition(String policyId, String stepId, String solution);
}
