package com.silenteight.serp.governance.policy.step.details;

import com.silenteight.serp.governance.policy.domain.dto.StepDto;

import java.util.UUID;

public interface StepRequestQuery {

  StepDto getStep(UUID stepId);
}
