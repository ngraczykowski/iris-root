package com.silenteight.serp.governance.policy.domain;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.governance.api.v1.FeatureVectorSolution;
import com.silenteight.serp.governance.policy.domain.dto.*;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureLogicConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.StepConfiguration;
import com.silenteight.serp.governance.policy.domain.exception.*;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.silenteight.governance.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.governance.api.v1.FeatureVectorSolution.SOLUTION_NO_DECISION;
import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static com.silenteight.serp.governance.policy.domain.PolicyState.DRAFT;
import static com.silenteight.serp.governance.policy.domain.PolicyState.IN_USE;
import static com.silenteight.serp.governance.policy.domain.PolicyState.SAVED;
import static com.silenteight.serp.governance.policy.domain.StepType.BUSINESS_LOGIC;
import static com.silenteight.serp.governance.policy.domain.StepType.MANUAL_RULE;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyServiceTest {

  private static final UUID POLICY_ID = UUID.randomUUID();
  private static final String POLICY_NAME = "policy_name";
  private static final String USER = "username";
  private static final String OTHER_USER = "username2";
  private static final String NEW_POLICY_NAME = "new name";
  private static final String NEW_DESCRIPTION = "new description name";

  private static final UUID FIRST_STEP = UUID.randomUUID();
  private static final UUID SECOND_STEP = UUID.randomUUID();
  private static final UUID THIRD_STEP = UUID.randomUUID();
  private static final UUID OTHER_STEP = UUID.randomUUID();
  private static final String STEP_NAME = "step name";
  private static final String STEP_DESCRIPTION = "step description";
  private static final String OTHER_STEP_NAME = "other step name";
  private static final String OTHER_STEP_DESCRIPTION = "other step description";

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
  void importPolicy() {
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

    verify(auditingLogger, times(8)).log(logCaptor.capture());

    var policy = policyRepository.getByPolicyId(policyId);
    assertThat(policy.getName()).isEqualTo(policyName);
    assertThat(policy.getState()).isEqualTo(IN_USE);
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
    Policy policy = createPolicyWithSteps(of(createStep(FIRST_STEP, 0)));

    ConfigureStepLogicRequest request = ConfigureStepLogicRequest
        .of(policy.getId(), FIRST_STEP, getFeatureLogicConfiguration(0, of()), USER);
    assertThatExceptionOfType(EmptyFeatureConfiguration.class)
        .isThrownBy(() -> underTest.configureStepLogic(request));
  }

  @Test
  void configureStepWithZeroFeaturesConfigurationWillThrowException() {
    Policy policy = createPolicyWithSteps(of(createStep(FIRST_STEP, 0)));

    ConfigureStepLogicRequest request = ConfigureStepLogicRequest
        .of(policy.getId(), FIRST_STEP, of(), USER);
    assertThatExceptionOfType(EmptyFeaturesLogicConfiguration.class)
        .isThrownBy(() -> underTest.configureStepLogic(request));
  }

  @Test
  void configureStepWithZeroMatchConditionValuesWillThrowException() {
    Policy policy = createPolicyWithSteps(of(createStep(FIRST_STEP, 0)));
    Collection<FeatureLogicConfiguration> featureLogicConfiguration = getFeatureLogicConfiguration(
        1, getFeatureConfiguration("name", of()));

    ConfigureStepLogicRequest request = ConfigureStepLogicRequest
        .of(policy.getId(), FIRST_STEP, featureLogicConfiguration, USER);
    assertThatExceptionOfType(EmptyMatchConditionValueException.class)
        .isThrownBy(() -> underTest.configureStepLogic(request));
  }

  @Test
  void configureStepWithZeroInToFulfillValueWillThrowException() {
    Policy policy = createPolicyWithSteps(of(createStep(FIRST_STEP, 0)));
    Collection<FeatureLogicConfiguration> featureLogicConfiguration = getFeatureLogicConfiguration(
        0, getFeatureConfiguration("name", of("value")));

    ConfigureStepLogicRequest request = ConfigureStepLogicRequest
        .of(policy.getId(), FIRST_STEP, featureLogicConfiguration, USER);
    assertThatExceptionOfType(WrongToFulfillValue.class)
        .isThrownBy(() -> underTest.configureStepLogic(request));
  }

  @Test
  void configureStepWithTooBigInToFulfillValueWillThrowException() {
    Policy policy = createPolicyWithSteps(of(createStep(FIRST_STEP, 2)));
    Collection<FeatureLogicConfiguration> featureLogicConfiguration = getFeatureLogicConfiguration(
        0, getFeatureConfiguration("name", of("value")));

    ConfigureStepLogicRequest request = ConfigureStepLogicRequest
        .of(policy.getId(), FIRST_STEP, featureLogicConfiguration, USER);
    assertThatExceptionOfType(WrongToFulfillValue.class)
        .isThrownBy(() -> underTest.configureStepLogic(request));
  }

  @NotNull
  private Policy createPolicyWithSteps(Collection<Step> steps) {
    UUID uuid = underTest.addPolicy(POLICY_ID, POLICY_NAME, USER);
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
    UUID uuid = underTest.addPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);

    assertThat(policy.getState()).isEqualTo(DRAFT);

    underTest.savePolicy(SavePolicyRequest.of(uuid, OTHER_USER));

    policy = policyRepository.getByPolicyId(uuid);
    assertThat(policy.getState()).isEqualTo(SAVED);
    assertThat(policy.getUpdatedBy()).isEqualTo(OTHER_USER);
  }

  @Test
  void savePolicyOnNonDraftWillThrowException() {
    UUID uuid = underTest.addPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    policy.setState(SAVED);

    assertThatExceptionOfType(WrongPolicyStateChangeException.class)
        .isThrownBy(() -> underTest.savePolicy(SavePolicyRequest.of(uuid, OTHER_USER)));
  }

  @Test
  void usePolicyOnSaveWillChangeState() {
    UUID uuid = underTest.addPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    policy.setState(SAVED);

    underTest.usePolicy(UsePolicyRequest.of(uuid, OTHER_USER));

    policy = policyRepository.getByPolicyId(uuid);
    assertThat(policy.getState()).isEqualTo(IN_USE);
    assertThat(policy.getUpdatedBy()).isEqualTo(OTHER_USER);
  }

  @Test
  void usePolicyOnNonSaveWillThrowException() {
    UUID uuid = underTest.addPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    assertThat(policy.getState()).isEqualTo(DRAFT);

    assertThatExceptionOfType(WrongPolicyStateChangeException.class)
        .isThrownBy(() -> underTest.usePolicy(UsePolicyRequest.of(uuid, USER)));
  }

  @Test
  void updatePolicyOnDraftWillUpdatePolicy() {
    UUID uuid = underTest.addPolicy(POLICY_ID, POLICY_NAME, USER);
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
    UUID uuid = underTest.addPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    policy.setState(SAVED);

    assertThatExceptionOfType(WrongPolicyStateException.class)
        .isThrownBy(() -> underTest.updatePolicy(
            UpdatePolicyRequest.of(uuid, NEW_POLICY_NAME, null, OTHER_USER)));
  }

  @Test
  void setStepsOrderOnDraftWillChangeOrder() {
    Policy policy = createPolicyWithSteps(of(
        createStep(FIRST_STEP, 0),
        createStep(SECOND_STEP, 1),
        createStep(THIRD_STEP, 2)
    ));
    assertThat(policy.getState()).isEqualTo(DRAFT);

    SetStepsOrderRequest request = SetStepsOrderRequest
        .of(policy.getPolicyId(), of(THIRD_STEP, FIRST_STEP, SECOND_STEP), OTHER_USER);
    underTest.setStepsOrder(request);

    policy = policyRepository.getByPolicyId(POLICY_ID);
    assertStepOrder(policy, FIRST_STEP, 1);
    assertStepOrder(policy, SECOND_STEP, 2);
    assertStepOrder(policy, THIRD_STEP, 0);
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
    UUID uuid = underTest.addPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    policy.setState(SAVED);

    SetStepsOrderRequest request = SetStepsOrderRequest
        .of(policy.getPolicyId(), of(FIRST_STEP, SECOND_STEP, THIRD_STEP), OTHER_USER);
    assertThatExceptionOfType(WrongPolicyStateException.class)
        .isThrownBy(() -> underTest.setStepsOrder(request));
  }

  @Test
  void setStepsOrderOnDraftWithWrongStepsSizeWillThrowException() {
    UUID uuid = underTest.addPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    assertThat(policy.getState()).isEqualTo(DRAFT);

    SetStepsOrderRequest request = SetStepsOrderRequest
        .of(policy.getPolicyId(), of(FIRST_STEP, SECOND_STEP, THIRD_STEP), OTHER_USER);
    assertThatExceptionOfType(StepsOrderListsSizeMismatch.class)
        .isThrownBy(() -> underTest.setStepsOrder(request));
  }

  @Test
  void setStepsOrderOnDraftWithWrongStepsWillThrowException() {
    Policy policy = createPolicyWithSteps(of(
        createStep(FIRST_STEP, 0),
        createStep(SECOND_STEP, 1),
        createStep(THIRD_STEP, 2)
    ));
    assertThat(policy.getState()).isEqualTo(DRAFT);

    SetStepsOrderRequest request = SetStepsOrderRequest
        .of(policy.getPolicyId(), of(FIRST_STEP, SECOND_STEP, OTHER_STEP), OTHER_USER);
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
        MANUAL_RULE,
        sortOrder,
        USER);
  }

  @Test
  void updatedStepOnNonDraftWillThrowException() {
    Policy policy = createPolicyWithSteps(of(
        createStep(FIRST_STEP, 0),
        createStep(SECOND_STEP, 1),
        createStep(THIRD_STEP, 2)
    ));
    policy.setState(SAVED);

    UpdateStepRequest request = UpdateStepRequest.of(
        policy.getId(), FIRST_STEP, STEP_NAME, null, SOLUTION_FALSE_POSITIVE, OTHER_USER);
    assertThatExceptionOfType(WrongPolicyStateException.class)
        .isThrownBy(() -> underTest.updateStep(request));
  }

  @Test
  void updatedStepOnDraftWillUpdateStep() {
    Policy policy = createPolicyWithSteps(of(
        createStep(FIRST_STEP, 0),
        createStep(SECOND_STEP, 1),
        createStep(THIRD_STEP, 2)
    ));
    assertThat(policy.getState()).isEqualTo(DRAFT);

    UpdateStepRequest request = UpdateStepRequest.of(
        policy.getId(),
        FIRST_STEP,
        OTHER_STEP_NAME,
        OTHER_STEP_DESCRIPTION,
        SOLUTION_NO_DECISION,
        OTHER_USER);
    underTest.updateStep(request);

    policy = policyRepository.getByPolicyId(policy.getPolicyId());
    Step updatedStep = policy
        .getSteps().stream().filter(step -> step.hasStepId(FIRST_STEP)).findFirst().orElseThrow();
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
        createStep(FIRST_STEP, 0),
        createStep(SECOND_STEP, 1),
        createStep(THIRD_STEP, 2)
    ));
    policy.setState(SAVED);

    assertThatExceptionOfType(WrongPolicyStateException.class)
        .isThrownBy(() -> underTest.deleteStep(
            DeleteStepRequest.of(policy.getId(), FIRST_STEP, OTHER_USER)));
  }

  @Test
  void deleteStepOnDraftWillDeleteStep() {
    Policy policy = createPolicyWithSteps(of(
        createStep(FIRST_STEP, 0),
        createStep(SECOND_STEP, 1),
        createStep(THIRD_STEP, 2)
    ));
    assertThat(policy.getState()).isEqualTo(DRAFT);

    underTest.deleteStep(DeleteStepRequest.of(policy.getId(), SECOND_STEP, OTHER_USER));

    policy = policyRepository.getByPolicyId(policy.getPolicyId());

    List<Step> sortedSteps = policy
        .getSteps()
        .stream()
        .sorted(Comparator.comparing(Step::getSortOrder))
        .collect(Collectors.toList());
    assertThat(sortedSteps)
        .extracting(Step::getStepId)
        .containsExactly(FIRST_STEP, THIRD_STEP);
    assertThat(policy.getUpdatedBy()).isEqualTo(OTHER_USER);
  }
}
