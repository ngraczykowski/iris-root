package com.silenteight.serp.governance.policy.domain;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.serp.governance.policy.domain.dto.*;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureLogicConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.StepConfiguration;
import com.silenteight.serp.governance.policy.domain.events.PolicyImportedEvent;
import com.silenteight.serp.governance.policy.domain.exception.*;
import com.silenteight.solving.api.v1.FeatureVectorSolution;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.*;
import java.util.stream.Stream;

import static com.silenteight.sens.governance.common.testing.rest.TestRoles.MODEL_TUNER;
import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static com.silenteight.serp.governance.policy.domain.PolicyState.*;
import static com.silenteight.serp.governance.policy.domain.SharedTestFixtures.POLICY_NAME;
import static com.silenteight.serp.governance.policy.domain.SharedTestFixtures.USER;
import static com.silenteight.serp.governance.policy.domain.StepType.BUSINESS_LOGIC;
import static com.silenteight.serp.governance.policy.domain.StepType.NARROW;
import static com.silenteight.serp.governance.policy.domain.TestFixtures.*;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_NO_DECISION;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {

  private final PolicyRepository policyRepository = new InMemoryPolicyRepository();
  @Mock
  private AuditingLogger auditingLogger;
  @Mock
  private ApplicationEventPublisher eventPublisher;

  private PolicyService underTest;

  @BeforeEach
  void setUp() {
    underTest = new PolicyDomainConfiguration()
        .policyService(policyRepository, auditingLogger, eventPublisher);
  }

  @Test
  void importPolicyWithUuids() {
    // given
    String featureName = "nameAgent";
    Collection<String> featureValues = of("EXACT_MATCH", "NEAR_MATCH");
    int logicCount = 1;
    FeatureVectorSolution stepSolution = SOLUTION_FALSE_POSITIVE;
    String stepName = "step-1";
    String stepDescription = "This is test description";
    StepType stepType = BUSINESS_LOGIC;
    String policyName = "policy-name";
    String creator = "asmith";
    List<FeatureConfiguration> featureConfiguration = getFeatureConfiguration(
        featureName, featureValues);
    FeatureLogicConfiguration featureLogicConfiguration = FeatureLogicConfiguration
        .builder()
        .toFulfill(logicCount)
        .featureConfigurations(featureConfiguration)
        .build();
    StepConfiguration stepConfiguration = StepConfiguration
        .builder()
        .solution(stepSolution)
        .stepName(stepName)
        .stepId(FIRST_STEP_ID)
        .stepDescription(stepDescription)
        .stepType(stepType)
        .featureLogicConfigurations(singletonList(featureLogicConfiguration))
        .build();
    ConfigurePolicyRequest request = ConfigurePolicyRequest
        .builder()
        .policyName(policyName)
        .policyId(POLICY_ID)
        .createdBy(creator)
        .stepConfigurations(singletonList(stepConfiguration))
        .build();

    // when
    UUID policyId = underTest.doImport(request);

    // then
    var logCaptor = ArgumentCaptor.forClass(AuditDataDto.class);

    verify(auditingLogger, times(6)).log(logCaptor.capture());

    assertThat(policyId).isEqualTo(POLICY_ID);
    var policy = policyRepository.getByPolicyId(POLICY_ID);
    assertThat(policy.getName()).isEqualTo(policyName);
    assertThat(policy.getState()).isEqualTo(SAVED);
    assertThat(policy.getPolicyId()).isEqualTo(policyId);
    assertThat(policy.getCreatedBy()).isEqualTo(creator);
    assertThat(policy.getUpdatedBy()).isEqualTo(creator);
    assertThat(policy.getSteps()).hasSize(1);

    var step = policy.getSteps().iterator().next();
    assertThat(step.getSolution()).isEqualTo(stepSolution);
    assertThat(step.getName()).isEqualTo(stepName);
    assertThat(step.getStepId()).isEqualTo(FIRST_STEP_ID);
    assertThat(step.getDescription()).isEqualTo(stepDescription);
    assertThat(step.getType()).isEqualTo(stepType);
    assertThat(step.getFeatureLogics()).hasSize(1);

    var featureLogic = step.getFeatureLogics().iterator().next();
    assertThat(featureLogic.getCount()).isEqualTo(logicCount);
    assertThat(featureLogic.getFeatures()).hasSize(1);

    var feature = featureLogic.getFeatures().iterator().next();
    assertThat(feature.getName()).isEqualTo(featureName);
    assertThat(feature.getValues()).isEqualTo(featureValues);

    var eventCaptor = ArgumentCaptor.forClass(PolicyImportedEvent.class);

    verify(eventPublisher).publishEvent(eventCaptor.capture());

    var event = eventCaptor.getValue();
    assertThat(event.getPolicyId()).isEqualTo(policyId);
  }

  @Test
  void importPolicyWithoutUuids() {
    // given
    String featureName = "nameAgent";
    Collection<String> featureValues = of("EXACT_MATCH", "NEAR_MATCH");
    int logicCount = 1;
    FeatureVectorSolution stepSolution = SOLUTION_FALSE_POSITIVE;
    String stepName = "step-1";
    String stepDescription = "This is test description";
    StepType stepType = BUSINESS_LOGIC;
    String policyName = "policy-name";
    String creator = "asmith";
    List<FeatureConfiguration> featureConfiguration = getFeatureConfiguration(
        featureName, featureValues);
    FeatureLogicConfiguration featureLogicConfiguration = FeatureLogicConfiguration
        .builder()
        .toFulfill(logicCount)
        .featureConfigurations(featureConfiguration)
        .build();
    StepConfiguration stepConfiguration = StepConfiguration
        .builder()
        .solution(stepSolution)
        .stepName(stepName)
        .stepDescription(stepDescription)
        .stepType(stepType)
        .featureLogicConfigurations(singletonList(featureLogicConfiguration))
        .build();
    ConfigurePolicyRequest request = ConfigurePolicyRequest
        .builder()
        .policyName(policyName)
        .createdBy(creator)
        .stepConfigurations(singletonList(stepConfiguration))
        .build();

    // when
    UUID policyId = underTest.doImport(request);

    // then
    var logCaptor = ArgumentCaptor.forClass(AuditDataDto.class);

    verify(auditingLogger, times(6)).log(logCaptor.capture());

    var policy = policyRepository.getByPolicyId(policyId);
    assertThat(policy.getName()).isEqualTo(policyName);
    assertThat(policy.getState()).isEqualTo(SAVED);
    assertThat(policy.getPolicyId()).isEqualTo(policyId);
    assertThat(policy.getCreatedBy()).isEqualTo(creator);
    assertThat(policy.getUpdatedBy()).isEqualTo(creator);
    assertThat(policy.getSteps()).hasSize(1);

    var step = policy.getSteps().iterator().next();
    assertThat(step.getSolution()).isEqualTo(stepSolution);
    assertThat(step.getName()).isEqualTo(stepName);
    assertThat(step.getDescription()).isEqualTo(stepDescription);
    assertThat(step.getType()).isEqualTo(stepType);
    assertThat(step.getFeatureLogics()).hasSize(1);

    var featureLogic = step.getFeatureLogics().iterator().next();
    assertThat(featureLogic.getCount()).isEqualTo(logicCount);
    assertThat(featureLogic.getFeatures()).hasSize(1);

    var feature = featureLogic.getFeatures().iterator().next();
    assertThat(feature.getName()).isEqualTo(featureName);
    assertThat(feature.getValues()).isEqualTo(featureValues);

    var eventCaptor = ArgumentCaptor.forClass(PolicyImportedEvent.class);

    verify(eventPublisher).publishEvent(eventCaptor.capture());

    var event = eventCaptor.getValue();
    assertThat(event.getPolicyId()).isEqualTo(policyId);
  }

  @Test
  void configureStepWithZeroFeatureConfigurationWillThrowException() {
    Policy policy = createPolicyWithSteps(of(createStep(FIRST_STEP_ID, 0)));

    ConfigureStepLogicRequest request = ConfigureStepLogicRequest
        .of(policy.getId(), FIRST_STEP_ID, getFeatureLogicConfiguration(0, of()), USER);
    assertThatExceptionOfType(EmptyFeatureConfiguration.class)
        .isThrownBy(() -> underTest.configureStepLogic(request));
  }

  @Test
  void configureStepWithZeroFeaturesConfigurationIsValidRequest() {
    Policy policy = createPolicyWithSteps(of(createStep(FIRST_STEP_ID, 0)));

    ConfigureStepLogicRequest request = ConfigureStepLogicRequest
        .of(policy.getId(), FIRST_STEP_ID, of(), USER);
    assertThatNoException().isThrownBy(() -> underTest.configureStepLogic(request));
  }

  @Test
  void configureStepWithZeroMatchConditionValuesWillThrowException() {
    Policy policy = createPolicyWithSteps(of(createStep(FIRST_STEP_ID, 0)));
    Collection<FeatureLogicConfiguration> featureLogicConfiguration = getFeatureLogicConfiguration(
        1, getFeatureConfiguration("name", of()));

    ConfigureStepLogicRequest request = ConfigureStepLogicRequest
        .of(policy.getId(), FIRST_STEP_ID, featureLogicConfiguration, USER);
    assertThatExceptionOfType(EmptyMatchConditionValueException.class)
        .isThrownBy(() -> underTest.configureStepLogic(request));
  }

  @Test
  void configureStepWithZeroInToFulfillValueWillThrowException() {
    Policy policy = createPolicyWithSteps(of(createStep(FIRST_STEP_ID, 0)));
    Collection<FeatureLogicConfiguration> featureLogicConfiguration = getFeatureLogicConfiguration(
        0, getFeatureConfiguration("name", of("value")));

    ConfigureStepLogicRequest request = ConfigureStepLogicRequest
        .of(policy.getId(), FIRST_STEP_ID, featureLogicConfiguration, USER);
    assertThatExceptionOfType(WrongToFulfillValue.class)
        .isThrownBy(() -> underTest.configureStepLogic(request));
  }

  @Test
  void configureStepWithTooBigInToFulfillValueWillThrowException() {
    Policy policy = createPolicyWithSteps(of(createStep(FIRST_STEP_ID, 2)));
    Collection<FeatureLogicConfiguration> featureLogicConfiguration = getFeatureLogicConfiguration(
        0, getFeatureConfiguration("name", of("value")));

    ConfigureStepLogicRequest request = ConfigureStepLogicRequest
        .of(policy.getId(), FIRST_STEP_ID, featureLogicConfiguration, USER);
    assertThatExceptionOfType(WrongToFulfillValue.class)
        .isThrownBy(() -> underTest.configureStepLogic(request));
  }

  @NotNull
  private Policy createPolicyWithSteps(Collection<Step> steps) {
    UUID uuid = underTest.createPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    steps.forEach(policy::addStep);
    policyRepository.save(policy);
    return policy;
  }

  private List<FeatureConfiguration> getFeatureConfiguration(
      String name, Collection<String> value) {

    return of(FeatureConfiguration.builder().name(name).condition(IS).values(value).build());
  }

  private Collection<FeatureLogicConfiguration> getFeatureLogicConfiguration(
      int toFulfill, Collection<FeatureConfiguration> featureConfiguration) {
    FeatureLogicConfiguration featureLogicConfiguration = FeatureLogicConfiguration
        .builder()
        .toFulfill(toFulfill)
        .featureConfigurations(featureConfiguration)
        .build();

    return of(featureLogicConfiguration);
  }

  @Test
  void savePolicyOnDraftWillChangeState() {
    UUID uuid = underTest.createPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);

    assertThat(policy.getState()).isEqualTo(DRAFT);

    underTest.savePolicy(SavePolicyRequest.of(uuid, OTHER_USER));

    policy = policyRepository.getByPolicyId(uuid);
    assertThat(policy.getState()).isEqualTo(SAVED);
    assertThat(policy.getUpdatedBy()).isEqualTo(OTHER_USER);
  }

  @Test
  void savePolicyOnNonDraftWillThrowException() {
    UUID uuid = underTest.createPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    policy.save();

    assertThat(policy.getState()).isEqualTo(SAVED);

    assertThatExceptionOfType(WrongPolicyStateChangeException.class)
        .isThrownBy(() -> underTest.savePolicy(SavePolicyRequest.of(uuid, OTHER_USER)));
  }

  @Test
  void archivePolicyOnDraftWillThrowException() {
    UUID uuid = underTest.createPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);

    assertThat(policy.getState()).isEqualTo(DRAFT);

    ArchivePolicyRequest archivePolicyRequest = ArchivePolicyRequest.of(uuid, OTHER_USER);
    assertThatExceptionOfType(WrongPolicyStateChangeException.class)
        .isThrownBy(() -> underTest.archivePolicy(archivePolicyRequest));
  }

  @Test
  void archivePolicyOnInUseWillThrowException() {
    UUID uuid = underTest.createPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    policy.save();
    policy.publish();
    policy.use();

    assertThat(policy.getState()).isEqualTo(IN_USE);

    ArchivePolicyRequest archivePolicyRequest = ArchivePolicyRequest.of(uuid, OTHER_USER);
    assertThatExceptionOfType(WrongPolicyStateChangeException.class)
        .isThrownBy(() -> underTest.archivePolicy(archivePolicyRequest));
  }

  @Test
  void archivePolicySavedDraftWillChangeState() {
    UUID uuid = underTest.createPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    policy.save();

    underTest.archivePolicy(ArchivePolicyRequest.of(uuid, OTHER_USER));

    policy = policyRepository.getByPolicyId(uuid);
    assertThat(policy.getState()).isEqualTo(ARCHIVED);
    assertThat(policy.getUpdatedBy()).isEqualTo(OTHER_USER);
  }

  @Test
  void usePolicyOnSaveWillChangeState() {
    UUID uuid = underTest.createPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    policy.setState(SAVED);

    underTest.usePolicy(UsePolicyRequest.of(uuid, OTHER_USER));

    policy = policyRepository.getByPolicyId(uuid);
    assertThat(policy.getState()).isEqualTo(IN_USE);
    assertThat(policy.getUpdatedBy()).isEqualTo(OTHER_USER);
  }

  @Test
  void usePolicyOnNonSaveWillThrowException() {
    UUID uuid = underTest.createPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    assertThat(policy.getState()).isEqualTo(DRAFT);

    assertThatExceptionOfType(WrongPolicyStateChangeException.class)
        .isThrownBy(() -> underTest.usePolicy(UsePolicyRequest.of(uuid, USER)));
  }

  @Test
  void deletePolicyOnDraftWillChangeState() {
    UUID uuid = underTest.createPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    assertThat(policy.getState()).isEqualTo(DRAFT);

    underTest.deletePolicy(DeletePolicyRequest.of(uuid, OTHER_USER));

    assertThat(policyRepository.findByPolicyId(uuid)).isEmpty();
  }

  @Test
  void deletePolicyOnNonSaveWillThrowException() {
    UUID uuid = underTest.createPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    policy.save();
    assertThat(policy.getState()).isEqualTo(SAVED);

    DeletePolicyRequest deletePolicyRequest = DeletePolicyRequest.of(uuid, USER);
    assertThatExceptionOfType(WrongPolicyStateException.class)
        .isThrownBy(() -> underTest.deletePolicy(deletePolicyRequest));
  }

  @Test
  void updatePolicyOnDraftWillUpdatePolicy() {
    UUID uuid = underTest.createPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    assertThat(policy.getState()).isEqualTo(DRAFT);

    underTest.updatePolicy(
        UpdatePolicyRequest.of(uuid, NEW_POLICY_NAME, NEW_DESCRIPTION, OTHER_USER));

    policy = policyRepository.getByPolicyId(uuid);
    assertThat(policy.getName()).isEqualTo(NEW_POLICY_NAME);
    assertThat(policy.getDescription()).isEqualTo(NEW_DESCRIPTION);
    assertThat(policy.getUpdatedBy()).isEqualTo(OTHER_USER);
  }

  @Test
  void updatePolicyOnNonDraftWillThrowException() {
    UUID uuid = underTest.createPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    policy.setState(SAVED);

    assertThatExceptionOfType(WrongPolicyStateException.class)
        .isThrownBy(() -> underTest.updatePolicy(
            UpdatePolicyRequest.of(uuid, NEW_POLICY_NAME, null, OTHER_USER)));
  }

  @Test
  void setStepsOrderOnDraftWillChangeOrder() {
    Policy policy = createPolicyWithSteps(of(
        createStep(FIRST_STEP_ID, 0),
        createStep(SECOND_STEP_ID, 1),
        createStep(THIRD_STEP_ID, 2)
    ));
    assertThat(policy.getState()).isEqualTo(DRAFT);

    SetStepsOrderRequest request = SetStepsOrderRequest
        .of(policy.getPolicyId(), of(THIRD_STEP_ID, FIRST_STEP_ID, SECOND_STEP_ID), OTHER_USER);
    underTest.setStepsOrder(request);

    policy = policyRepository.getByPolicyId(POLICY_ID);
    assertStepOrder(policy, FIRST_STEP_ID, 1);
    assertStepOrder(policy, SECOND_STEP_ID, 2);
    assertStepOrder(policy, THIRD_STEP_ID, 0);
    assertThat(policy.getUpdatedBy()).isEqualTo(OTHER_USER);
  }

  private void assertStepOrder(Policy policy, UUID firstStep, int order) {
    policy
        .getSteps()
        .stream()
        .filter(step -> step.hasStepId(firstStep))
        .findFirst()
        .ifPresent(step -> assertThat(step.getSortOrder()).isEqualTo(order));
  }

  @Test
  void setStepsOrderOnNonDraftWillThrowException() {
    UUID uuid = underTest.createPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    policy.setState(SAVED);

    SetStepsOrderRequest request = SetStepsOrderRequest
        .of(policy.getPolicyId(), of(FIRST_STEP_ID, SECOND_STEP_ID, THIRD_STEP_ID), OTHER_USER);
    assertThatExceptionOfType(WrongPolicyStateException.class)
        .isThrownBy(() -> underTest.setStepsOrder(request));
  }

  @Test
  void setStepsOrderOnDraftWithWrongStepsSizeWillThrowException() {
    UUID uuid = underTest.createPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    assertThat(policy.getState()).isEqualTo(DRAFT);

    SetStepsOrderRequest request = SetStepsOrderRequest
        .of(policy.getPolicyId(), of(FIRST_STEP_ID, SECOND_STEP_ID, THIRD_STEP_ID), OTHER_USER);
    assertThatExceptionOfType(StepsOrderListsSizeMismatch.class)
        .isThrownBy(() -> underTest.setStepsOrder(request));
  }

  @Test
  void setStepsOrderOnDraftWithWrongStepsWillThrowException() {
    Policy policy = createPolicyWithSteps(of(
        createStep(FIRST_STEP_ID, 0),
        createStep(SECOND_STEP_ID, 1),
        createStep(THIRD_STEP_ID, 2)
    ));
    assertThat(policy.getState()).isEqualTo(DRAFT);

    SetStepsOrderRequest request = SetStepsOrderRequest
        .of(policy.getPolicyId(), of(FIRST_STEP_ID, SECOND_STEP_ID, OTHER_STEP_ID), OTHER_USER);
    assertThatExceptionOfType(WrongIdsListInSetStepsOrder.class)
        .isThrownBy(() -> underTest.setStepsOrder(request));
  }

  @NotNull
  private Step createStep(UUID firstStep, int sortOrder) {
    return new Step(
        SOLUTION_FALSE_POSITIVE,
        firstStep,
        STEP_NAME,
        STEP_DESCRIPTION,
        BUSINESS_LOGIC,
        sortOrder,
        USER);
  }

  private static Step createStep(UUID firstStep, int sortOrder, StepType type) {
    return new Step(
        SOLUTION_FALSE_POSITIVE,
        firstStep,
        STEP_NAME,
        STEP_DESCRIPTION,
        type,
        sortOrder,
        USER);
  }

  @Test
  void updatedStepOnNonDraftWillThrowException() {
    Policy policy = createPolicyWithSteps(of(
        createStep(FIRST_STEP_ID, 0),
        createStep(SECOND_STEP_ID, 1),
        createStep(THIRD_STEP_ID, 2)
    ));
    policy.setState(SAVED);

    UpdateStepRequest request = UpdateStepRequest.of(
        policy.getId(), FIRST_STEP_ID, STEP_NAME, null, SOLUTION_FALSE_POSITIVE, OTHER_USER);
    assertThatExceptionOfType(WrongPolicyStateException.class)
        .isThrownBy(() -> underTest.updateStep(request));
  }

  @Test
  void updatedStepOnDraftWillUpdateStep() {
    Policy policy = createPolicyWithSteps(of(
        createStep(FIRST_STEP_ID, 0),
        createStep(SECOND_STEP_ID, 1),
        createStep(THIRD_STEP_ID, 2)
    ));
    assertThat(policy.getState()).isEqualTo(DRAFT);

    UpdateStepRequest request = UpdateStepRequest.of(
        policy.getId(),
        FIRST_STEP_ID,
        OTHER_STEP_NAME,
        OTHER_STEP_DESCRIPTION,
        SOLUTION_NO_DECISION,
        OTHER_USER);
    underTest.updateStep(request);

    policy = policyRepository.getByPolicyId(policy.getPolicyId());
    Step updatedStep = policy
        .getSteps()
        .stream()
        .filter(step -> step.hasStepId(FIRST_STEP_ID))
        .findFirst()
        .orElseThrow();
    assertThat(updatedStep.getName()).isEqualTo(OTHER_STEP_NAME);
    assertThat(updatedStep.getDescription()).isEqualTo(OTHER_STEP_DESCRIPTION);
    assertThat(updatedStep.getSortOrder()).isZero();
    assertThat(updatedStep.getUpdatedBy()).isEqualTo(OTHER_USER);
    assertThat(updatedStep.getCreatedBy()).isEqualTo(USER);
    assertThat(policy.getUpdatedBy()).isEqualTo(OTHER_USER);
  }

  @Test
  void deleteStepOnNonDraftWillThrowException() {
    Policy policy = createPolicyWithSteps(of(
        createStep(FIRST_STEP_ID, 0),
        createStep(SECOND_STEP_ID, 1),
        createStep(THIRD_STEP_ID, 2)
    ));
    policy.setState(SAVED);

    assertThatExceptionOfType(WrongPolicyStateException.class)
        .isThrownBy(() -> underTest.deleteStep(
            DeleteStepRequest.of(policy.getId(), FIRST_STEP_ID, OTHER_USER)));
  }

  @Test
  void deleteStepOnDraftWillDeleteStep() {
    Policy policy = createPolicyWithSteps(of(
        createStep(FIRST_STEP_ID, 0),
        createStep(SECOND_STEP_ID, 1),
        createStep(THIRD_STEP_ID, 2)
    ));
    assertThat(policy.getState()).isEqualTo(DRAFT);

    underTest.deleteStep(DeleteStepRequest.of(policy.getId(), SECOND_STEP_ID, OTHER_USER));

    policy = policyRepository.getByPolicyId(policy.getPolicyId());

    List<Step> sortedSteps = policy
        .getSteps()
        .stream()
        .sorted(Comparator.comparing(Step::getSortOrder))
        .collect(toList());
    assertThat(sortedSteps)
        .extracting(Step::getStepId)
        .containsExactly(FIRST_STEP_ID, THIRD_STEP_ID);
    assertThat(policy.getUpdatedBy()).isEqualTo(OTHER_USER);
  }

  @Test
  void clonePolicyWillMakeDeepCopy() {
    //given
    Step firstStep = createStep(FIRST_STEP_ID, 0);
    firstStep.setName(STEP_NAME);
    firstStep.setFeatureLogics(of(FEATURE_LOGIC));
    firstStep.setSolution(SOLUTION_NO_DECISION);
    Policy originPolicy = createPolicyWithSteps(of(firstStep));

    //when
    UUID clonedPolicyId = underTest.clonePolicy(
        ClonePolicyRequest.of(POLICY_ID_CLONED, POLICY_ID, OTHER_USER));

    Policy clonedPolicy = policyRepository.getByPolicyId(clonedPolicyId);

    //then
    assertThat(clonedPolicyId).isNotEqualTo(POLICY_ID);
    assertThat(clonedPolicy.getId()).isNotEqualTo(originPolicy.getId());
    assertThat(clonedPolicy.getName()).isEqualTo(POLICY_NAME);
    assertThat(clonedPolicy.getDescription()).isEqualTo(originPolicy.getDescription());
    assertThat(clonedPolicy.getCreatedBy()).isEqualTo(OTHER_USER);
    assertThat(clonedPolicy.getState()).isEqualTo(DRAFT);
    assertThat(clonedPolicy.getSteps()).containsAll(originPolicy.getSteps());

    Step originStep = originPolicy.getSteps().stream().findFirst().orElseThrow();
    Step clonedStep = clonedPolicy.getSteps().stream().findFirst().orElseThrow();

    assertNotEquals(originStep.getStepId(), clonedStep.getStepId());
    assertEquals(originStep.getName(), clonedStep.getName());
    assertEquals(originStep.getDescription(), clonedStep.getDescription());
    assertEquals(originStep.getSolution(), clonedStep.getSolution());
    assertEquals(originStep.getSortOrder(), clonedStep.getSortOrder());
    assertEquals(originStep.getType(), clonedStep.getType());
    assertEquals(
        originStep.getFeatureLogics().size(),
        clonedStep.getFeatureLogics().size());
    assertEquals(1, originStep.getFeatureLogics().size());

    FeatureLogic originFeatureLogic = getFirstFeatureLogic(originStep);
    FeatureLogic clonedFeatureLogic = getFirstFeatureLogic(clonedStep);
    assertEquals(originFeatureLogic.getCount(), clonedFeatureLogic.getCount());
    assertEquals(
        originFeatureLogic.getFeatures().size(),
        clonedFeatureLogic.getFeatures().size());

    MatchCondition originFeature = getFirstMatchCondition(originFeatureLogic);
    MatchCondition clonedFeature = getFirstMatchCondition(clonedFeatureLogic);
    assertEquals(originFeature.getName(), clonedFeature.getName());
    assertEquals(originFeature.getCondition(), clonedFeature.getCondition());
    assertEquals(originFeature.getValues(), clonedFeature.getValues());
  }

  @Test
  void shouldReturnClonedStepId() {
    //given
    CloneStepRequest request =
        CloneStepRequest.of(OTHER_STEP_ID, FIRST_STEP_ID, POLICY_ID, MODEL_TUNER);

    Step step = createStep(FIRST_STEP_ID, 0);
    Policy policy = createPolicyWithSteps(of(step));
    policyRepository.save(policy);
    Step clonedStep = createStep(OTHER_STEP_ID, 0);

    //when
    UUID clonedStepId = underTest.cloneStep(request);
    Policy policyWithClonedStep = policyRepository.getByPolicyId(POLICY_ID);

    //then
    assertThat(policyWithClonedStep.getSteps()).hasSize(2);
    assertThat(policyWithClonedStep.getSteps()).contains(clonedStep);
    assertThat(clonedStepId).isEqualTo(OTHER_STEP_ID);
  }

  private static FeatureLogic getFirstFeatureLogic(Step step) {
    return step
        .getFeatureLogics()
        .stream()
        .findFirst()
        .orElseThrow();
  }

  private static MatchCondition getFirstMatchCondition(FeatureLogic featureLogic) {
    return featureLogic
        .getFeatures()
        .stream()
        .findFirst()
        .orElseThrow();
  }

  @ParameterizedTest
  @MethodSource("provideStepsWithValidOrder")
  void assertStepOrdersWithNarrowSteps(List<Step> actual, List<Step> expected) {
    //given
    Policy policy = createPolicyWithSteps(actual);
    List<UUID> stepIds = actual.stream().map(Step::getStepId).collect(toList());
    SetStepsOrderRequest request = SetStepsOrderRequest.of(policy.getPolicyId(), stepIds, USER);
    //when
    underTest.setStepsOrder(request);
    //then
    assertStepsOrder(policy.getSteps(), expected);
  }

  private static Stream provideStepsWithValidOrder() {
    return of(
        Arguments.of(
            of(
                createStep(FIRST_STEP_ID, 1, NARROW),
                createStep(SECOND_STEP_ID, 2, StepType.BUSINESS_LOGIC)
            ),
            of(
                createStep(FIRST_STEP_ID, 0, NARROW),
                createStep(SECOND_STEP_ID, 1, StepType.BUSINESS_LOGIC)
            )),
        Arguments.of(
            of(
                createStep(FIRST_STEP_ID, 1, NARROW)
            ),
            of(
                createStep(FIRST_STEP_ID, 0, NARROW)
            )),
        Arguments.of(
            of(
                createStep(FIRST_STEP_ID, 1, StepType.BUSINESS_LOGIC)
            ),
            of(
                createStep(FIRST_STEP_ID, 0, StepType.BUSINESS_LOGIC)
            )),
        Arguments.of(
            of(
                createStep(FIRST_STEP_ID, 0, NARROW),
                createStep(SECOND_STEP_ID, 1, NARROW),
                createStep(THIRD_STEP_ID, 2, StepType.BUSINESS_LOGIC)
            ),
            of(
                createStep(FIRST_STEP_ID, 0, NARROW),
                createStep(SECOND_STEP_ID, 1, NARROW),
                createStep(THIRD_STEP_ID, 2, StepType.BUSINESS_LOGIC)
            ))

    ).stream();
  }

  @Test
  void invalidNarrowStepOrderWillThrowException() {
    //given
    List<Step> steps = of(
        createStep(FIRST_STEP_ID, 0, NARROW),
        createStep(SECOND_STEP_ID, 1),
        createStep(THIRD_STEP_ID, 2)
    );
    Policy policy = createPolicyWithSteps(steps);

    assertStepsOrder(policy.getSteps(), steps);

    SetStepsOrderRequest request = SetStepsOrderRequest
        .of(policy.getPolicyId(), of(SECOND_STEP_ID, THIRD_STEP_ID, FIRST_STEP_ID), USER);
    //when
    //then
    assertThatExceptionOfType(NarrowStepsOrderHierarchyMismatch.class)
        .isThrownBy(() -> underTest.setStepsOrder(request))
        .withMessage(format("Could not set steps order. "
            + "Requested orders steps mismatched order hierarchy. Narrow steps should be primary "
            + "on the list in policy=%s.", policy.getPolicyId()));

    policy = policyRepository.getById(policy.getId());
    assertStepsOrder(policy.getSteps(), steps);
  }

  private void assertStepsOrder(Collection<Step> actualSteps, Collection<Step> expectedSteps) {
    for (Step step : actualSteps) {
      Step expectedStep = expectedSteps
          .stream()
          .filter(currentStep -> step.hasStepId(currentStep.getStepId()))
          .findFirst()
          .orElseThrow(NoSuchElementException::new);
      assertThat(step.getSortOrder()).isEqualTo(expectedStep.getSortOrder());
    }
  }

  @Test
  void markPolicyUsedOnToBeUsedWillChangeState() {
    UUID uuid = underTest.createPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    policy.setUpdatedBy(OTHER_USER);
    policy.setState(TO_BE_USED);

    underTest.markPolicyAsUsed(MarkPolicyAsUsedRequest.of(uuid, OTHER_USER));

    policy = policyRepository.getByPolicyId(uuid);
    assertThat(policy.getState()).isEqualTo(IN_USE);
    assertThat(policy.getUpdatedBy()).isEqualTo(OTHER_USER);
  }
}
