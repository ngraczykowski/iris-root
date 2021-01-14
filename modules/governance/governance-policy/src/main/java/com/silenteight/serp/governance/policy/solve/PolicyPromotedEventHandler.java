package com.silenteight.serp.governance.policy.solve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyPromotedEvent;
import com.silenteight.serp.governance.policy.domain.dto.StepConfigurationDto;
import com.silenteight.serp.governance.policy.step.PolicyStepsConfigurationQuery;

import java.util.List;

@RequiredArgsConstructor
class PolicyPromotedEventHandler {

  @NonNull
  private final PolicyStepsConfigurationQuery stepsConfigurationQuery;

  @NonNull
  private final StepPolicyFactory stepPolicyFactory;

  void handle(PolicyPromotedEvent event) {
    List<StepConfigurationDto> steps =
        stepsConfigurationQuery.listStepsConfiguration(event.getPolicyId());
    stepPolicyFactory.reconfigure(steps);
  }
}
