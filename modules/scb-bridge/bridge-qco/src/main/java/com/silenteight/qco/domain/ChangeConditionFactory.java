package com.silenteight.qco.domain;

import com.silenteight.qco.domain.model.ChangeCondition;

interface ChangeConditionFactory {

  ChangeCondition createChangeCondition(String policyId, String stepId, String solution);
}