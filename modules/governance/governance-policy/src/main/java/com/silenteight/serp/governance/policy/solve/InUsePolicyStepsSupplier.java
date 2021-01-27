package com.silenteight.serp.governance.policy.solve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.PolicyPromotedEvent;
import com.silenteight.serp.governance.policy.step.PolicyStepsConfigurationQuery;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class InUsePolicyStepsSupplier implements StepsConfigurationSupplier, InUsePolicyLoader {

  @NonNull
  private final PolicyStepsConfigurationQuery stepsConfigurationQuery;
  @NonNull
  private final StepMapper stepMapper;

  private List<Step> steps = new ArrayList<>();

  @Override
  public List<Step> get() {
    return steps;
  }

  @EventListener
  public void handle(PolicyPromotedEvent event) {
    steps = loadSteps(event.getPolicyId());
  }

  @Override
  public void loadInUsePolicy() {
    // TODO(kdzieciol): load in use policy https://silent8.atlassian.net/browse/WEB-462
  }

  @NotNull
  private List<Step> loadSteps(UUID policyId) {
    return stepsConfigurationQuery
        .listStepsConfiguration(policyId)
        .stream()
        .map(stepMapper::map)
        .collect(toList());
  }
}
