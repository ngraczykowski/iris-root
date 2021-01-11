package com.silenteight.serp.governance.policy.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.governance.v1.api.FeatureVectorSolution;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest.FeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest.FeatureLogicConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest.StepConfiguration;

import org.jetbrains.annotations.NotNull;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class PolicyService {

  @NonNull
  private final PolicyRepository repository;

  @Transactional
  public UUID doImport(ImportPolicyRequest request) {
    UUID policyId = UUID.randomUUID();
    Policy policy = addPolicy(
        policyId, request.getPolicyName(), request.getCreatedBy());
    request.getStepConfigurations()
           .forEach(configuration -> configureImportedStep(policy, configuration));
    Policy savedPolicy = repository.save(policy);
    return savedPolicy.getPolicyId();
  }

  private void configureImportedStep(Policy policy, StepConfiguration configuration) {
    UUID stepId = UUID.randomUUID();
    doAddStepToPolicy(
        policy,
        configuration.getSolution(),
        stepId,
        configuration.getStepName(),
        configuration.getStepDescription(),
        configuration.getStepType());
    doConfigureStepLogic(
        policy, stepId, configuration.getFeatureLogicConfigurations());
  }

  @NotNull
  public Policy addPolicy(
      @NonNull UUID policyId, @NonNull String policyName, @NonNull String createdBy) {

    Policy policy = new Policy(policyId, policyName, createdBy);
    return repository.save(policy);
  }

  @Transactional
  public void addStepToPolicy(
      @NonNull UUID policyId, @NonNull FeatureVectorSolution solution, @NonNull UUID stepId,
      @NonNull String stepName, String stepDescription, @NonNull StepType stepType) {

    Policy policy = repository.getByPolicyId(policyId);
    doAddStepToPolicy(policy, solution, stepId, stepName, stepDescription, stepType);
  }

  @NotNull
  private Step doAddStepToPolicy(
      @NonNull Policy policy, @NonNull FeatureVectorSolution solution, @NonNull UUID stepId,
      @NonNull String stepName, String stepDescription, @NonNull StepType stepType) {

    Step step = new Step(solution, stepId, stepName, stepDescription, stepType);
    policy.addStep(step);
    return step;
  }

  @Transactional
  public void configureStepLogic(
      @NonNull UUID policyId,
      @NonNull UUID stepId,
      @NonNull Collection<FeatureLogicConfiguration> featureLogicConfigurations) {

    Policy policy = repository.getByPolicyId(policyId);
    doConfigureStepLogic(policy, stepId, featureLogicConfigurations);
  }

  private void doConfigureStepLogic(
      @NonNull Policy policy,
      @NonNull UUID stepId,
      @NonNull Collection<FeatureLogicConfiguration> featureLogicConfigurations) {

    policy.reconfigureStep(stepId, mapToFeatureLogics(featureLogicConfigurations));
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
