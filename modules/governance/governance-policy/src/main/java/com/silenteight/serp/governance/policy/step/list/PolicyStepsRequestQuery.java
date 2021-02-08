package com.silenteight.serp.governance.policy.step.list;

import com.silenteight.serp.governance.policy.domain.dto.StepDto;

import java.util.Collection;
import java.util.UUID;

public interface PolicyStepsRequestQuery {

  Collection<StepDto> listSteps(UUID policyId);

  Long getPolicyIdForStep(UUID stepId);
}
