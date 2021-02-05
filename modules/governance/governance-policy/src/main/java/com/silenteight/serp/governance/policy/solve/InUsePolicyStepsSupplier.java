package com.silenteight.serp.governance.policy.solve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.domain.PolicyPromotedEvent;
import com.silenteight.serp.governance.policy.step.PolicyStepsConfigurationQuery;

import org.springframework.context.event.EventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Slf4j
class InUsePolicyStepsSupplier implements StepsConfigurationSupplier, InUsePolicyLoader {

  private static final String NO_IN_USE_POLICY_WARNING_MESSAGE =
      "No Policy Version is in status 'Used for Solving'. All alerts will be solved "
          + "as 'No Decision' due to no active policy.";

  @NonNull
  private final PolicyStepsConfigurationQuery stepsConfigurationQuery;
  @NonNull
  private final InUsePolicyQuery inUsePolicyQuery;
  @NonNull
  private final StepMapper stepMapper;

  private UUID policyId;

  private List<Step> steps = new ArrayList<>();

  @Override
  public List<Step> get() {
    logIfNoPolicy();
    logIfNoSteps();
    return steps;
  }

  private void logIfNoPolicy() {
    if (policyId == null)
      log.warn("No policy configured. Alert will be solved as 'No Decision'");
  }

  private void logIfNoSteps() {
    if (policyId != null && steps.isEmpty())
      log.warn("No steps configured in policy {}. Alert will be solved as 'No Decision'.",
               policyId.toString());
  }

  @EventListener
  public void handle(PolicyPromotedEvent event) {
    loadSteps(event.getPolicyId());
  }

  @Override
  public void loadInUsePolicy() {
    Optional<UUID> policyInUse = inUsePolicyQuery.getPolicyInUse();
    if (policyInUse.isEmpty()) {
      log.warn(NO_IN_USE_POLICY_WARNING_MESSAGE);
      policyId = null;
      return;
    }

    loadSteps(policyInUse.get());
  }

  private void loadSteps(UUID policyId) {
    this.policyId = policyId;
    steps = stepsConfigurationQuery
        .listStepsConfiguration(policyId)
        .stream()
        .map(stepMapper::map)
        .collect(toList());
  }
}
