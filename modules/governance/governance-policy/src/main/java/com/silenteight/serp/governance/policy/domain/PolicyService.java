package com.silenteight.serp.governance.policy.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.proto.governance.v1.api.FeatureVectorSolution;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest.FeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest.FeatureLogicConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest.StepConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.PolicyDto;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@RequiredArgsConstructor
public class PolicyService {

  @NonNull
  private final PolicyRepository repository;
  @NonNull
  private final AuditingLogger auditingLogger;
  @NonNull
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public UUID doImport(ImportPolicyRequest request) {
    UUID correlationId = randomUUID();
    UUID policyId = randomUUID();
    logPolicyCreateRequested(request, policyId, correlationId);

    Policy policy = addPolicy(
        policyId, request.getPolicyName(), request.getCreatedBy());
    configureImportedSteps(policy, request.getStepConfigurations());
    Policy savedPolicy = repository.save(policy);

    eventPublisher.publishEvent(new PolicyCreatedEvent(savedPolicy.getPolicyId(), correlationId));
    return savedPolicy.getPolicyId();
  }

  private void logPolicyCreateRequested(
      ImportPolicyRequest request, UUID policyId, UUID correlationId) {

    AuditDataDto auditDataDto = AuditDataDto.builder()
        .correlationId(correlationId)
        .eventId(randomUUID())
        .timestamp(Timestamp.from(Instant.now()))
        .type("PolicyCreateRequested")
        .entityId(policyId.toString())
        .entityClass("Policy")
        .entityAction("CREATE")
        .details(request.toString())
        .build();
    auditingLogger.log(auditDataDto);
  }

  private void configureImportedSteps(Policy policy, List<StepConfiguration> configurations) {
    range(0, configurations.size())
        .boxed()
        .forEach(i -> configureImportedStep(policy, configurations.get(i), i));
  }

  private void configureImportedStep(
      Policy policy, StepConfiguration configuration, int sortOrder) {

    UUID stepId = UUID.randomUUID();
    doAddStepToPolicy(
        policy,
        configuration.getSolution(),
        stepId,
        configuration.getStepName(),
        configuration.getStepDescription(),
        configuration.getStepType(),
        sortOrder);
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
      @NonNull UUID policyId,
      @NonNull FeatureVectorSolution solution,
      @NonNull UUID stepId,
      @NonNull String stepName,
      String stepDescription,
      @NonNull StepType stepType,
      int sortOrder) {

    Policy policy = repository.getByPolicyId(policyId);
    doAddStepToPolicy(policy, solution, stepId, stepName, stepDescription, stepType, sortOrder);
  }

  @NotNull
  private Step doAddStepToPolicy(
      @NonNull Policy policy,
      @NonNull FeatureVectorSolution solution,
      @NonNull UUID stepId,
      @NonNull String stepName,
      String stepDescription,
      @NonNull StepType stepType,
      int sortOrder) {

    Step step = new Step(solution, stepId, stepName, stepDescription, stepType, sortOrder);
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

  public PolicyDto getPolicy(UUID policyId) {
    return repository
        .findByPolicyId(policyId)
        .map(Policy::toDto)
        .orElseThrow();
  }
}
