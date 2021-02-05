package com.silenteight.serp.governance.policy.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.governance.api.v1.FeatureVectorSolution;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest.FeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest.FeatureLogicConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest.StepConfiguration;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.silenteight.serp.governance.policy.domain.PolicyState.IN_USE;
import static java.util.Optional.ofNullable;
import static java.util.Set.of;
import static java.util.UUID.randomUUID;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@RequiredArgsConstructor
public class PolicyService {

  @NonNull
  private final PolicyRepository policyRepository;
  @NonNull
  private final AuditingLogger auditingLogger;
  @NonNull
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public UUID doImport(ImportPolicyRequest request) {
    UUID correlationId = randomUUID();
    UUID policyId = randomUUID();
    logPolicyCreateRequested(request, policyId, correlationId);

    Policy policy = addPolicyInternal(
        policyId, request.getPolicyName(), request.getDescription(), request.getCreatedBy());
    configureImportedSteps(policy, request.getStepConfigurations(), request.getCreatedBy());
    Policy savedPolicy = policyRepository.save(policy);
    savePolicy(policyId, request.getCreatedBy());
    usePolicy(policyId, request.getCreatedBy());
    eventPublisher.publishEvent(new PolicyImportedEvent(savedPolicy.getPolicyId(), correlationId));
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

  private void configureImportedSteps(
      Policy policy, List<StepConfiguration> configurations, String createdBy) {

    range(0, configurations.size())
        .boxed()
        .forEach(i -> configureImportedStep(policy, configurations.get(i), i, createdBy));
  }

  private void configureImportedStep(
      Policy policy, StepConfiguration configuration, int sortOrder, String createdBy) {

    UUID stepId = UUID.randomUUID();
    doAddStepToPolicy(
        policy,
        configuration.getSolution(),
        stepId,
        configuration.getStepName(),
        configuration.getStepDescription(),
        configuration.getStepType(),
        sortOrder,
        createdBy);
    doConfigureStepLogic(
        policy, stepId, configuration.getFeatureLogicConfigurations());
  }

  public UUID addPolicy(
      @NonNull UUID policyId,
      @NonNull String policyName,
      @NonNull String createdBy) {
    return addPolicyInternal(policyId, policyName, null, createdBy).getPolicyId();
  }

  @NotNull
  Policy addPolicyInternal(
      @NonNull UUID policyId,
      @NonNull String policyName,
      String description,
      @NonNull String createdBy) {

    Policy policy = new Policy(policyId, policyName, description, createdBy);
    return policyRepository.save(policy);
  }

  @Transactional
  public void addStepToPolicy(
      @NonNull UUID policyId,
      @NonNull FeatureVectorSolution solution,
      @NonNull UUID stepId,
      @NonNull String stepName,
      String stepDescription,
      @NonNull StepType stepType,
      int sortOrder,
      String createdBy) {

    Policy policy = policyRepository.getByPolicyId(policyId);
    doAddStepToPolicy(
        policy, solution, stepId, stepName, stepDescription, stepType, sortOrder, createdBy);
  }

  @NotNull
  private Step doAddStepToPolicy(
      @NonNull Policy policy,
      @NonNull FeatureVectorSolution solution,
      @NonNull UUID stepId,
      @NonNull String stepName,
      String stepDescription,
      @NonNull StepType stepType,
      int sortOrder,
      String createdBy) {

    Step step = new Step(
        solution, stepId, stepName, stepDescription, stepType, sortOrder, createdBy);
    policy.addStep(step);
    return step;
  }

  @Transactional
  public void configureStepLogic(
      @NonNull UUID policyId,
      @NonNull UUID stepId,
      @NonNull Collection<FeatureLogicConfiguration> featureLogicConfigurations) {

    Policy policy = policyRepository.getByPolicyId(policyId);
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

  private static Collection<MatchCondition> mapToFeatures(
      Collection<FeatureConfiguration> configurations) {

    return configurations
        .stream()
        .map(PolicyService::mapToFeature)
        .collect(toList());
  }

  private static MatchCondition mapToFeature(FeatureConfiguration configuration) {
    return new MatchCondition(
        configuration.getName(), configuration.getCondition(), configuration.getValues());
  }

  @Transactional
  public void savePolicy(@NonNull UUID policyId, @NonNull String savedBy) {
    Policy policy = policyRepository.getByPolicyId(policyId);
    policy.save();
    policy.setUpdatedBy(savedBy);
    policyRepository.save(policy);
  }

  @Transactional
  public void usePolicy(@NonNull UUID policyId, @NonNull String activatedBy) {
    stopUsingOtherPolicies(activatedBy);
    useSelectedPolicy(policyId, activatedBy);
  }

  private void stopUsingOtherPolicies(String activatedBy) {
    policyRepository.findAllByStateIn(of(IN_USE)).forEach(policy -> {
      policy.stopUsing();
      policy.setUpdatedBy(activatedBy);
    });
  }

  private void useSelectedPolicy(UUID policyId, String activatedBy) {
    policyRepository.findByPolicyId(policyId).ifPresent(policy -> {
      policy.use();
      policy.setUpdatedBy(activatedBy);
    });
  }

  @Transactional
  public void updatePolicy(UUID id, String name, String description, String updatedBy) {
    Policy policy = policyRepository.getByPolicyId(id);
    ofNullable(name).ifPresent(policy::setName);
    ofNullable(description).ifPresent(policy::setDescription);
    policy.setUpdatedBy(updatedBy);
  }
}
