package com.silenteight.serp.governance.policy.featurevector;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.dto.StepConfigurationDto;
import com.silenteight.serp.governance.policy.solve.ReconfigurableStepsConfigurationFactory;
import com.silenteight.serp.governance.policy.solve.StepsConfigurationSupplier;
import com.silenteight.serp.governance.policy.step.PolicyStepsConfigurationQuery;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
class StepsConfigurationSupplierFactory {

  @NonNull
  private final PolicyStepsRequestQuery stepQuery;
  @NonNull
  private final PolicyStepsConfigurationQuery stepsConfigurationQuery;
  @NonNull
  private final ReconfigurableStepsConfigurationFactory stepsConfigurationFactory;

  public StepsConfigurationSupplier getConfigurationSupplierBasedOnStep(@NonNull UUID stepId) {
    Long policyId = stepQuery.getPolicyIdForStep(stepId);
    List<StepConfigurationDto> stepsConfigurationDto = stepsConfigurationQuery
        .listStepsConfiguration(policyId);
    return stepsConfigurationFactory.getStepsConfigurationProvider(stepsConfigurationDto);
  }
}
