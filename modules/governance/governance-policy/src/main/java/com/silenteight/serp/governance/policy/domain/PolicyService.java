package com.silenteight.serp.governance.policy.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.serp.governance.policy.domain.dto.*;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureLogicConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.StepConfiguration;
import com.silenteight.serp.governance.policy.domain.events.PolicyImportedEvent;
import com.silenteight.serp.governance.policy.domain.exception.EmptyFeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.exception.EmptyMatchConditionValueException;
import com.silenteight.serp.governance.policy.domain.exception.WrongBasePolicyException;
import com.silenteight.serp.governance.policy.domain.exception.WrongToFulfillValue;
import com.silenteight.solving.api.v1.FeatureVectorSolution;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.silenteight.serp.governance.policy.domain.PolicyState.IN_USE;
import static java.util.Optional.ofNullable;
import static java.util.Set.of;
import static java.util.stream.Collectors.toList;
import static java.util.stream.IntStream.range;

@RequiredArgsConstructor
public class PolicyService {

  private static final int DEFAULT_STEP_ORDER_VALUE = Integer.MAX_VALUE;

  @NonNull
  private final PolicyRepository policyRepository;
  @NonNull
  private final AuditingLogger auditingLogger;
  @NonNull
  private final ApplicationEventPublisher eventPublisher;

  @Transactional
  public UUID doImport(ConfigurePolicyRequest request) {
    request.preAudit(auditingLogger::log);

    AddPolicyRequest addPolicyRequest = AddPolicyRequest.of(
        request.getCorrelationId(),
        request.getPolicyId(),
        request.getPolicyName(),
        request.getDescription(),
        request.getCreatedBy());

    Policy policy = addPolicyInternal(addPolicyRequest);
    configureImportedSteps(
        request.getCorrelationId(),
        policy,
        request.getStepConfigurations(),
        request.getCreatedBy());
    Policy savedPolicy = policyRepository.save(policy);
    savePolicy(SavePolicyRequest.of(request.getPolicyId(), request.getCreatedBy()));
    //TODO(kdzieciol): After the import, the policy should stay in the `SAVED` state. (WEB-1092)
    usePolicy(UsePolicyRequest.of(request.getPolicyId(), request.getCreatedBy()));
    PolicyImportedEvent importedEvent = PolicyImportedEvent.builder()
        .policyId(savedPolicy.getPolicyId())
        .correlationId(request.getCorrelationId())
        .build();
    eventPublisher.publishEvent(importedEvent);
    request.postAudit(auditingLogger::log);
    return savedPolicy.getPolicyId();
  }

  private void configureImportedSteps(
      UUID correlationID, Policy policy, List<StepConfiguration> configurations, String createdBy) {

    range(0, configurations.size())
        .boxed()
        .forEach(i -> configureImportedStep(
            correlationID, policy, configurations.get(i), i, createdBy));
  }

  private void configureImportedStep(
      UUID correlationId,
      Policy policy,
      StepConfiguration configuration,
      int sortOrder,
      String createdBy) {

    addStep(
        correlationId,
        policy,
        configuration.getSolution(),
        configuration.getStepId(),
        configuration.getStepName(),
        configuration.getStepDescription(),
        configuration.getStepType(),
        sortOrder,
        createdBy);
    doConfigureStepLogic(
        policy,
        configuration.getStepId(),
        configuration.getFeatureLogicConfigurations(),
        createdBy);
  }

  public UUID createPolicy(
      @NonNull UUID policyId,
      @NonNull String policyName,
      @NonNull String createdBy) {

    AddPolicyRequest addPolicyRequest = AddPolicyRequest.of(policyId, policyName, null, createdBy);
    Policy policy = addPolicyInternal(addPolicyRequest);
    return policy.getPolicyId();
  }

  @NotNull
  Policy addPolicyInternal(AddPolicyRequest addPolicyRequest) {
    addPolicyRequest.preAudit(auditingLogger::log);
    Policy policy = new Policy(
        addPolicyRequest.getId(),
        addPolicyRequest.getPolicyName(),
        addPolicyRequest.getDescription(),
        addPolicyRequest.getCreatedBy());
    policy = policyRepository.save(policy);
    addPolicyRequest.postAudit(auditingLogger::log);
    return policy;
  }

  @Transactional
  public void addStepToPolicy(CreateStepRequest createStepRequest) {
    createStepRequest.preAudit(auditingLogger::log);
    Policy policy = policyRepository.getByPolicyId(createStepRequest.getPolicyId());
    addStep(
        UUID.randomUUID(),
        policy,
        createStepRequest.getSolution(),
        createStepRequest.getStepId(),
        createStepRequest.getStepName(),
        createStepRequest.getStepDescription(),
        createStepRequest.getStepType(),
        DEFAULT_STEP_ORDER_VALUE,
        createStepRequest.getCreatedBy());
    createStepRequest.postAudit(auditingLogger::log);
  }

  @NotNull
  private Step addStep(
      @NonNull UUID correlationId,
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
  public void configureStepLogic(ConfigureStepLogicRequest configureStepLogicRequest) {
    configureStepLogicRequest.preAudit(auditingLogger::log);
    Policy policy = policyRepository.getById(configureStepLogicRequest.getPolicyId());
    doConfigureStepLogic(
        policy,
        configureStepLogicRequest.getStepId(),
        configureStepLogicRequest.getFeatureLogicConfigurations(),
        configureStepLogicRequest.getEditedBy());
    configureStepLogicRequest.postAudit(auditingLogger::log);
  }

  private void doConfigureStepLogic(
      @NonNull Policy policy,
      @NonNull UUID stepId,
      @NonNull Collection<FeatureLogicConfiguration> featureLogicConfigurations,
      @NonNull String editedBy) {

    policy.reconfigureStep(
        stepId, mapToFeatureLogics(stepId, featureLogicConfigurations), editedBy);
  }

  private static Collection<FeatureLogic> mapToFeatureLogics(
      @NonNull UUID stepId, Collection<FeatureLogicConfiguration> configurations) {

    return configurations
        .stream()
        .map(configuration -> mapToFeatureLogic(stepId, configuration))
        .collect(toList());
  }

  private static FeatureLogic mapToFeatureLogic(
      @NonNull UUID stepId, FeatureLogicConfiguration configuration) {

    assertFeatureConfigurationsNotEmpty(stepId, configuration.getFeatureConfigurations());
    assertToFulfillValueIsCorrect(
        stepId, configuration.getToFulfill(), configuration.getFeatureConfigurations().size());

    return new FeatureLogic(
        configuration.getToFulfill(), mapToFeatures(configuration.getFeatureConfigurations()));
  }

  private static void assertFeatureConfigurationsNotEmpty(
      UUID stepId, Collection<FeatureConfiguration> featureConfigurations) {

    if (featureConfigurations.isEmpty())
      throw new EmptyFeatureConfiguration(stepId);
  }

  private static void assertToFulfillValueIsCorrect(UUID stepId, int toFulfill, int maxSize) {
    if (toFulfill > maxSize || toFulfill < 1)
      throw new WrongToFulfillValue(stepId, toFulfill, maxSize);
  }

  private static Collection<MatchCondition> mapToFeatures(
      Collection<FeatureConfiguration> configurations) {

    return configurations
        .stream()
        .map(PolicyService::mapToFeature)
        .collect(toList());
  }

  private static MatchCondition mapToFeature(FeatureConfiguration configuration) {
    assertValueListNotEmpty(configuration.getName(), configuration.getValues());

    return new MatchCondition(
        configuration.getName(), configuration.getCondition(), configuration.getValues());
  }

  private static void assertValueListNotEmpty(
      @NonNull String featureName, @NonNull Collection<String> values) {

    if (values.isEmpty())
      throw new EmptyMatchConditionValueException(featureName);
  }

  @Transactional
  public void savePolicy(SavePolicyRequest savePolicyRequest) {
    savePolicyRequest.preAudit(auditingLogger::log);
    Policy policy = policyRepository.getByPolicyId(savePolicyRequest.getPolicyId());
    policy.save();
    policy.setUpdatedBy(savePolicyRequest.getSavedBy());
    policyRepository.save(policy);
    savePolicyRequest.postAudit(auditingLogger::log);
  }

  @Transactional
  public void usePolicy(UsePolicyRequest usePolicyRequest) {
    usePolicyRequest.preAudit(auditingLogger::log);
    stopUsingOtherPolicies(usePolicyRequest.getActivatedBy());
    Policy policy = policyRepository.getByPolicyId(usePolicyRequest.getPolicyId());
    policy.use();
    policy.setUpdatedBy(usePolicyRequest.getActivatedBy());
    usePolicyRequest.postAudit(auditingLogger::log);
  }

  @Transactional
  public void promotePolicy(PromotePolicyRequest toBeUsePolicyRequest) {
    toBeUsePolicyRequest.preAudit(auditingLogger::log);
    Policy policy = policyRepository.getByPolicyId(toBeUsePolicyRequest.getPolicyId());
    policy.publish();
    policy.setUpdatedBy(toBeUsePolicyRequest.getActivatedBy());
    toBeUsePolicyRequest.postAudit(auditingLogger::log);
  }

  private void stopUsingOtherPolicies(String activatedBy) {
    policyRepository.findAllByStateIn(of(IN_USE)).forEach(policy -> {
      policy.stopUsing();
      policy.setUpdatedBy(activatedBy);
    });
  }

  @Transactional
  public void updatePolicy(UpdatePolicyRequest updatePolicyRequest) {
    updatePolicyRequest.preAudit(auditingLogger::log);
    Policy policy = policyRepository.getByPolicyId(updatePolicyRequest.getId());
    ofNullable(updatePolicyRequest.getPolicyName()).ifPresent(policy::setName);
    ofNullable(updatePolicyRequest.getDescription()).ifPresent(policy::setDescription);
    policy.setUpdatedBy(updatePolicyRequest.getUpdatedBy());
    updatePolicyRequest.postAudit(auditingLogger::log);
  }

  @Transactional
  public void setStepsOrder(SetStepsOrderRequest setStepsOrderReqest) {
    setStepsOrderReqest.preAudit(auditingLogger::log);
    Policy policy = policyRepository.getByPolicyId(setStepsOrderReqest.getPolicyId());
    policy.updateStepsOrder(
        setStepsOrderReqest.getStepsOrder(), setStepsOrderReqest.getUpdatedBy());
    policyRepository.save(policy);
    setStepsOrderReqest.postAudit(auditingLogger::log);
  }

  @Transactional
  public void updateStep(UpdateStepRequest updateStepRequest) {
    updateStepRequest.preAudit(auditingLogger::log);
    Policy policy = policyRepository.getById(updateStepRequest.getId());
    policy.updateStep(
        updateStepRequest.getStepId(),
        updateStepRequest.getName(),
        updateStepRequest.getDescription(),
        updateStepRequest.getSolution(),
        updateStepRequest.getUpdatedBy());
    policy.setUpdatedBy(updateStepRequest.getUpdatedBy());
    policyRepository.save(policy);
    updateStepRequest.postAudit(auditingLogger::log);
  }

  @Transactional
  public void deleteStep(DeleteStepRequest deleteStepRequest) {
    deleteStepRequest.preAudit(auditingLogger::log);
    Policy policy = policyRepository.getById(deleteStepRequest.getPolicyId());
    policy.deleteStep(deleteStepRequest.getStepId());
    policy.setUpdatedBy(deleteStepRequest.getDeletedBy());
    policyRepository.save(policy);
    deleteStepRequest.postAudit(auditingLogger::log);
  }

  @Transactional
  public UUID clonePolicy(ClonePolicyRequest clonePolicyRequest) {
    clonePolicyRequest.preAudit(auditingLogger::log);
    Policy origin = validateAndReturnPolicy(clonePolicyRequest.getBasePolicyId());
    Policy policy = new Policy(
        clonePolicyRequest.getPolicyId(),
        origin.getName(),
        origin.getDescription(),
        clonePolicyRequest.getCreatedBy());
    policy.setSteps(cloneSteps(origin.getSteps()));
    Policy savedPolicy = policyRepository.save(policy);
    clonePolicyRequest.postAudit(auditingLogger::log);
    return savedPolicy.getPolicyId();
  }

  public Policy validateAndReturnPolicy(UUID policyId) {
    return policyRepository.findByPolicyId(policyId)
        .orElseThrow(() -> new WrongBasePolicyException(policyId));
  }

  private static Collection<Step> cloneSteps(Collection<Step> steps) {
    return steps
        .stream()
        .map(PolicyService::cloneStep)
        .collect(toList());
  }

  private static Step cloneStep(Step origin) {
    Step step = new Step(origin.getSolution(), UUID.randomUUID(), origin.getName(),
        origin.getDescription(), origin.getType(), origin.getSortOrder(), origin.getCreatedBy());
    step.setFeatureLogics(cloneFeatureLogics(origin.getFeatureLogics()));
    return step;
  }

  private static Collection<FeatureLogic> cloneFeatureLogics(
      Collection<FeatureLogic> featureLogics) {
    return featureLogics
        .stream()
        .map(PolicyService::cloneFeatureLogic)
        .collect(toList());
  }

  private static FeatureLogic cloneFeatureLogic(FeatureLogic origin) {
    return new FeatureLogic(origin.getCount(), cloneFeatures(origin.getFeatures()));
  }

  private static Collection<MatchCondition> cloneFeatures(Collection<MatchCondition> features) {
    return features
        .stream()
        .map(PolicyService::cloneFeature)
        .collect(toList());
  }

  private static MatchCondition cloneFeature(MatchCondition origin) {
    return new MatchCondition(origin.getName(), origin.getCondition(), origin.getValues());
  }

  @Transactional
  public void archivePolicy(ArchivePolicyRequest request) {
    request.preAudit(auditingLogger::log);
    Policy policy = policyRepository.getByPolicyId(request.getPolicyId());
    policy.archive();
    policy.setUpdatedBy(request.getArchivedBy());
    policyRepository.save(policy);
    request.postAudit(auditingLogger::log);
  }

  @Transactional
  public void deletePolicy(DeletePolicyRequest request) {
    request.preAudit(auditingLogger::log);
    Policy policy = policyRepository.getByPolicyId(request.getPolicyId());
    policy.assertCanBeDeleted();
    policyRepository.deleteByPolicyId(request.getPolicyId());
    request.postAudit(auditingLogger::log);
  }
}
