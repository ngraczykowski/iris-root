package com.silenteight.serp.governance.policy.solve;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.domain.PolicyPromotedEvent;
import com.silenteight.serp.governance.policy.step.PolicyStepsConfigurationQuery;

import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;
import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import static java.lang.Boolean.FALSE;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toUnmodifiableList;

@RequiredArgsConstructor
@Slf4j
class PolicyStepsSupplierFactory implements StepsSupplierProvider {

  private static final String NO_IN_USE_POLICY_WARNING_MESSAGE =
      "No Policy Version is in status 'Used for Solving'.";

  @NonNull
  private final PolicyStepsConfigurationQuery stepsConfigurationQuery;
  @NonNull
  private final InUsePolicyQuery inUsePolicyQuery;
  @NonNull
  private final StepMapper stepMapper;
  @Nullable
  private AtomicReference<LoadedPolicy> loadedInUsePolicy;

  @Override
  public StepsSupplier getStepsSupplier(@NonNull UUID policyId) {
    if (isInUsePolicy(policyId))
      return ofNullable(loadedInUsePolicy)
          .map(AtomicReference::get)
          .map(LoadedPolicy::getStepsSupplier)
          .orElseThrow();
    else
      return new LoadedPolicy(policyId, listSteps(policyId)).getStepsSupplier();
  }

  private List<Step> listSteps(UUID policyId) {
    return stepsConfigurationQuery
        .listStepsConfiguration(policyId)
        .stream()
        .map(stepMapper::map)
        .collect(toUnmodifiableList());
  }

  private boolean isInUsePolicy(@NonNull UUID policyId) {
    if (loadedInUsePolicy == null) {
      loadPolicyInUse();
    }

    return ofNullable(loadedInUsePolicy)
        .map(loadedPolicy -> loadedPolicy.get().isSamePolicy(policyId))
        .orElse(FALSE);
  }

  private void loadPolicyInUse() {
    Optional<UUID> policyInUse = inUsePolicyQuery.getPolicyInUse();
    if (policyInUse.isEmpty()) {
      log.warn(NO_IN_USE_POLICY_WARNING_MESSAGE);
      return;
    }

    loadInUsePolicy(policyInUse.get());
  }

  @EventListener
  public void handle(PolicyPromotedEvent event) {
    loadInUsePolicy(event.getPolicyId());
  }

  private void loadInUsePolicy(UUID policyId) {
    loadedInUsePolicy = new AtomicReference<>(new LoadedPolicy(policyId, listSteps(policyId)));
  }

  @RequiredArgsConstructor
  @Immutable
  private static class LoadedPolicy {

    @NonNull
    private final UUID policyId;
    @NonNull
    private final List<Step> steps;

    public boolean isSamePolicy(@NonNull UUID policyId) {
      return policyId.equals(this.policyId);
    }

    StepsSupplier getStepsSupplier() {
      return () -> steps;
    }
  }
}
