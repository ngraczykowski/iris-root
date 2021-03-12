package com.silenteight.serp.governance.policy.step;

import com.silenteight.serp.governance.policy.domain.dto.StepConfigurationDto;

import java.util.List;
import java.util.UUID;

public interface PolicyStepsConfigurationQuery {

  List<StepConfigurationDto> listStepsConfiguration(UUID policyId);
}
