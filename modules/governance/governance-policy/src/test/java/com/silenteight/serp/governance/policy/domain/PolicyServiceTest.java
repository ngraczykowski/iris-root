package com.silenteight.serp.governance.policy.domain;

import com.silenteight.auditing.bs.AuditDataDto;
import com.silenteight.auditing.bs.AuditingLogger;
import com.silenteight.governance.api.v1.FeatureVectorSolution;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest.FeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest.FeatureLogicConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest.StepConfiguration;
import com.silenteight.serp.governance.policy.domain.exception.StepsOrderListsSizeMismatch;
import com.silenteight.serp.governance.policy.domain.exception.WrongIdsListInSetStepsOrder;
import com.silenteight.serp.governance.policy.domain.exception.WrongPolicyStateChangeException;
import com.silenteight.serp.governance.policy.domain.exception.WrongPolicyStateException;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Collection;
import java.util.UUID;

import static com.silenteight.governance.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
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

  private PolicyRepository policyRepository = new InMemoryPolicyRepository();
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
    FeatureConfiguration featureConfiguration = FeatureConfiguration
        .builder().name(featureName).condition(IS).values(featureValues).build();
    FeatureLogicConfiguration featureLogicConfiguration = FeatureLogicConfiguration
        .builder()
        .count(logicCount)
        .featureConfigurations(singletonList(featureConfiguration))
        .build();
    StepConfiguration stepConfiguration = StepConfiguration
        .builder()
        .solution(stepSolution)
        .stepName(stepName)
        .stepDescription(stepDescription)
        .stepType(stepType)
        .featureLogicConfigurations(singletonList(featureLogicConfiguration))
        .build();
    ImportPolicyRequest request = ImportPolicyRequest
        .builder()
        .policyName(policyName)
        .createdBy(creator)
        .stepConfigurations(singletonList(stepConfiguration))
        .build();

    // when
    UUID policyId = underTest.doImport(request);

    // then
    var logCaptor = ArgumentCaptor.forClass(AuditDataDto.class);

    verify(auditingLogger).log(logCaptor.capture());

    var log = logCaptor.getValue();
    assertThat(log.getType()).isEqualTo("PolicyCreateRequested");
    assertThat(log.getEntityId()).isEqualTo(policyId.toString());
    assertThat(log.getEntityClass()).isEqualTo("Policy");
    assertThat(log.getEntityAction()).isEqualTo("CREATE");
    assertThat(log.getDetails()).isEqualTo(request.toString());

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
  void savePolicyOnDraftWillChangeState() {
    UUID uuid = underTest.addPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    assertThat(policy.getState()).isEqualTo(DRAFT);

    underTest.savePolicy(uuid, OTHER_USER);

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
        .isThrownBy(() -> underTest.savePolicy(uuid, OTHER_USER));
  }

  @Test
  void usePolicyOnSaveWillChangeState() {
    UUID uuid = underTest.addPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    policy.setState(SAVED);

    underTest.usePolicy(uuid, OTHER_USER);

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
        .isThrownBy(() -> underTest.usePolicy(uuid, USER));
  }

  @Test
  void updatePolicyOnDraftWillUpdatePolicy() {
    UUID uuid = underTest.addPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    assertThat(policy.getState()).isEqualTo(DRAFT);

    underTest.updatePolicy(uuid, NEW_POLICY_NAME, NEW_DESCRIPTION, OTHER_USER);

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
        .isThrownBy(() -> underTest.updatePolicy(uuid, NEW_POLICY_NAME, null, OTHER_USER));
  }

  @Test
  void setStepsOrderOnDraftWillChangeOrder() {
    UUID uuid = underTest.addPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    policy.addStep(createStep(FIRST_STEP, 0));
    policy.addStep(createStep(SECOND_STEP, 1));
    policy.addStep(createStep(THIRD_STEP, 2));
    assertThat(policy.getState()).isEqualTo(DRAFT);

    underTest.setStepsOrder(
        uuid, of(THIRD_STEP, FIRST_STEP, SECOND_STEP), OTHER_USER);

    policy = policyRepository.getByPolicyId(POLICY_ID);
    assertStepOrder(policy, FIRST_STEP, 1);
    assertStepOrder(policy, SECOND_STEP, 2);
    assertStepOrder(policy, THIRD_STEP, 0);
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

    assertThatExceptionOfType(WrongPolicyStateException.class)
        .isThrownBy(() -> underTest.setStepsOrder(
            uuid, of(FIRST_STEP, SECOND_STEP, THIRD_STEP), OTHER_USER));
  }

  @Test
  void setStepsOrderOnDraftWithWrongStepsSizeWillThrowException() {
    UUID uuid = underTest.addPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    assertThat(policy.getState()).isEqualTo(DRAFT);

    assertThatExceptionOfType(StepsOrderListsSizeMismatch.class)
        .isThrownBy(() -> underTest.setStepsOrder(
            uuid, of(FIRST_STEP, SECOND_STEP, THIRD_STEP), OTHER_USER));
  }

  @Test
  void setStepsOrderOnDraftWithWrongStepsWillThrowException() {
    UUID uuid = underTest.addPolicy(POLICY_ID, POLICY_NAME, USER);
    Policy policy = policyRepository.getByPolicyId(uuid);
    policy.addStep(createStep(FIRST_STEP, 0));
    policy.addStep(createStep(SECOND_STEP, 1));
    policy.addStep(createStep(THIRD_STEP, 2));
    assertThat(policy.getState()).isEqualTo(DRAFT);

    assertThatExceptionOfType(WrongIdsListInSetStepsOrder.class)
        .isThrownBy(() -> underTest.setStepsOrder(
            uuid, of(FIRST_STEP, SECOND_STEP, OTHER_STEP), OTHER_USER));
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
}
