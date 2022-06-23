package com.silenteight.serp.governance.policy.step.list;

import com.silenteight.serp.governance.policy.domain.dto.StepDto;
import com.silenteight.serp.governance.policy.domain.dto.StepSearchCriteriaDto;

import java.util.Collection;
import java.util.UUID;

public interface PolicyStepsRequestQuery {

  Collection<StepDto> listSteps(UUID policyId);

  Collection<StepDto> listFilteredSteps(UUID policyId, StepSearchCriteriaDto criteria);

  Long getPolicyIdForStep(UUID stepId);
}
