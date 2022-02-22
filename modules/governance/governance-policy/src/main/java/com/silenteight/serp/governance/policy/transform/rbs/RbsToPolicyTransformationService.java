package com.silenteight.serp.governance.policy.transform.rbs;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.policy.common.StepResource;
import com.silenteight.serp.governance.policy.domain.Condition;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.StepType;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureLogicConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ConfigureStepLogicRequest;
import com.silenteight.serp.governance.policy.domain.dto.CreateStepRequest;
import com.silenteight.serp.governance.policy.domain.dto.SavePolicyRequest;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;
import com.silenteight.serp.governance.policy.transform.rbs.StepsData.Feature;
import com.silenteight.serp.governance.policy.transform.rbs.StepsData.Step;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.util.Collections.singleton;

@Slf4j
@RequiredArgsConstructor
class RbsToPolicyTransformationService {

  private static final String CREATED_BY = "StepsData";

  private final PolicyService policyService;
  private final PolicyStepsRequestQuery policyStepsRequestQuery;

  public UUID transform(StepsData stepsData) {
    UUID policyId = policyService.createPolicy(UUID.randomUUID(), stepsData.getName(), CREATED_BY);

    int index = 0;
    int stepsCount = stepsData.getSteps().size();
    for (Step step : stepsData.getSteps()) {
      log.debug("Import step {} of {}", index++, stepsCount);
      addStep(step, policyId);
    }

    policyService.savePolicy(SavePolicyRequest.of(policyId, CREATED_BY));

    return policyId;
  }

  private void addStep(Step step, UUID policyId) {
    CreateStepRequest createStepRequest = createStepCommand(step, policyId);

    policyService.addStepToPolicy(createStepRequest);

    ConfigureStepLogicRequest configureStepLogicRequest =
        configureStepLogicRequest(step.getFeatures(), createStepRequest.getStepId());

    policyService.configureStepLogic(configureStepLogicRequest);
  }

  private static CreateStepRequest createStepCommand(Step step, UUID policyId) {
    UUID stepId = UUID.randomUUID();

    return CreateStepRequest.of(
        policyId,
        step.getSolution().getFeatureVectorSolution(),
        stepId,
        StepResource.toResourceName(stepId),
        step.getReasoningBranchId(),
        StepType.BUSINESS_LOGIC,
        CREATED_BY);
  }

  private ConfigureStepLogicRequest configureStepLogicRequest(
      List<Feature> features, UUID stepId) {
    long policyId = policyStepsRequestQuery.getPolicyIdForStep(stepId);

    List<FeatureConfiguration> featureConfigurations = features.stream()
        .map(feature ->
            FeatureConfiguration.builder()
                .condition(Condition.IS)
                .name(feature.getName())
                .values(singleton(feature.getValue()))
                .build()
        )
        .collect(Collectors.toUnmodifiableList());

    FeatureLogicConfiguration featureLogicConfiguration = FeatureLogicConfiguration.builder()
        .featureConfigurations(featureConfigurations)
        .toFulfill(featureConfigurations.size())
        .build();

    return ConfigureStepLogicRequest.of(
        policyId,
        stepId,
        singleton(featureLogicConfiguration),
        CREATED_BY);
  }
}
