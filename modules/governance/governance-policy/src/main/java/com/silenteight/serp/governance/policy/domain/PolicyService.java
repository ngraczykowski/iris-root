package com.silenteight.serp.governance.policy.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.domain.CreatePolicyRequest.FeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.CreatePolicyRequest.FeatureLogicConfiguration;
import com.silenteight.serp.governance.policy.domain.CreatePolicyRequest.StepConfiguration;

import java.util.Collection;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class PolicyService {

  @NonNull
  private final PolicyRepository repository;

  public void doImport(CreatePolicyRequest request) {
    Policy policy = new Policy(
        request.getPolicyId(), request.getPolicyName(), request.getCreatedBy());
    addAndConfigureSteps(policy, request.getStepConfigurations());

    repository.save(policy);
  }

  private static void addAndConfigureSteps(
      Policy policy, Collection<StepConfiguration> configurations) {

    configurations
        .forEach(configuration -> addAndConfigureStep(policy, configuration));
  }

  private static void addAndConfigureStep(Policy policy, StepConfiguration configuration) {
    Step step = new Step(
        configuration.getSolution(),
        configuration.getStepId(),
        configuration.getStepName(),
        configuration.getStepDescription(),
        configuration.getStepType());
    policy.addStep(step);

    policy.reconfigureStep(
        configuration.getStepId(),
        mapToFeatureLogics(configuration.getFeatureLogicConfigurations()));
  }

  private static Collection<FeatureLogic> mapToFeatureLogics(
      Collection<FeatureLogicConfiguration> configurations) {

    return configurations
        .stream()
        .map(PolicyService::mapToFeatureLogic)
        .collect(toList());
  }

  private static FeatureLogic mapToFeatureLogic(FeatureLogicConfiguration configuration) {
    return new FeatureLogic(
        configuration.getCount(), mapToFeatures(configuration.getFeatureConfigurations()));
  }

  private static Collection<Feature> mapToFeatures(
      Collection<FeatureConfiguration> configurations) {

    return configurations
        .stream()
        .map(PolicyService::mapToFeature)
        .collect(toList());
  }

  private static Feature mapToFeature(FeatureConfiguration configuration) {
    return new Feature(configuration.getName(), configuration.getValues());
  }
}
