package com.silenteight.serp.governance.policy.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.policy.domain.dto.CloneStepRequest;
import com.silenteight.serp.governance.policy.domain.dto.CreateStepRequest;
import com.silenteight.serp.governance.policy.domain.dto.SetStepsOrderRequest;
import com.silenteight.serp.governance.policy.domain.dto.UpdateStepRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.UUID;
import javax.persistence.PersistenceException;

import static com.silenteight.serp.governance.policy.domain.DomainConstants.MAX_STEP_NAME_LENGTH;
import static com.silenteight.serp.governance.policy.domain.SharedTestFixtures.POLICY_NAME;
import static com.silenteight.serp.governance.policy.domain.SharedTestFixtures.USER;
import static com.silenteight.serp.governance.policy.domain.StepType.BUSINESS_LOGIC;
import static com.silenteight.serp.governance.policy.domain.TestFixtures.*;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_NO_DECISION;
import static java.lang.String.format;
import static java.util.List.of;
import static java.util.UUID.randomUUID;
import static org.apache.commons.lang3.RandomStringUtils.random;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowable;

@TestPropertySource("classpath:data-test.properties")
@ContextConfiguration(classes = { PolicyRepositoryTestConfiguration.class })
class PolicyServiceIT extends BaseDataJpaTest {

  private static final String STEP_NAME_TOO_LONG_MSG =
      format("ERROR: value too long for type character varying(%s)", MAX_STEP_NAME_LENGTH);

  @Autowired
  private PolicyService underTest;

  @Autowired
  private PolicyRepository policyRepository;

  @Autowired
  private StepRepository stepRepository;

  @Test
  void addStepToPolicy_succeeded_whenAllDataValid() {
    //given
    Policy policy = createPolicy();
    UUID stepUuid = randomUUID();
    CreateStepRequest request = CreateStepRequest.of(
        policy.getPolicyId(),
        SOLUTION_NO_DECISION,
        stepUuid,
        STEP_NAME_WITH_MAX_LENGTH,
        STEP_DESCRIPTION,
        BUSINESS_LOGIC,
        USER);
    //when
    underTest.addStepToPolicy(request);
    //then
    Step step = stepRepository.getStepByStepId(stepUuid);
    assertThat(step.getStepId()).isEqualTo(stepUuid);
    assertThat(step.getName()).isEqualTo(request.getStepName());
    assertThat(step.getDescription()).isEqualTo(request.getStepDescription());
    assertThat(step.getSolution()).isEqualTo(request.getSolution());
    assertThat(step.getCreatedBy()).isEqualTo(request.getCreatedBy());
    assertThat(step.getType()).isEqualTo(request.getStepType());
    assertThat(step.getFeatureLogics().isEmpty());
  }

  @Test
  void addStepToPolicy_throwsPersistenceException_whenStepNameToLong() {
    //given
    Policy policy = createPolicy();
    UUID stepUuid = randomUUID();
    CreateStepRequest request = CreateStepRequest.of(
        policy.getPolicyId(),
        SOLUTION_NO_DECISION,
        stepUuid,
        STEP_NAME_THAT_EXCEEDED_MAX_LENGTH,
        STEP_DESCRIPTION,
        BUSINESS_LOGIC,
        USER);
    //when
    underTest.addStepToPolicy(request);
    Throwable thrown = catchThrowable(() -> {
      //This force hibernate to execute inserts;
      entityManager.flush();
    });

    //then
    assertThat(thrown).isInstanceOf(PersistenceException.class);
    assertThat(thrown.getCause().getCause().getMessage()).isEqualTo(STEP_NAME_TOO_LONG_MSG);
  }

  @Test
  void updateStep_succeeded_whenAllDataValid() {
    //given
    Policy policy = createPolicy();
    Step step = addStep(policy, FIRST_STEP_ID);
    UpdateStepRequest request = UpdateStepRequest.of(
        policy.getId(),
        step.getStepId(),
        STEP_NAME_WITH_MAX_LENGTH,
        random(100),
        SOLUTION_NO_DECISION,
        USER);
    //when
    underTest.updateStep(request);
    //then
    step = stepRepository.getStepByStepId(step.getStepId());
    assertThat(step.getName()).isEqualTo(request.getName());
    assertThat(step.getDescription()).isEqualTo(request.getDescription());
    assertThat(step.getSolution()).isEqualTo(request.getSolution());
    assertThat(step.getCreatedBy()).isEqualTo(request.getUpdatedBy());
    assertThat(step.getFeatureLogics().isEmpty());
  }

  @Test
  void updateStep_throwsPersistenceException_whenStepNameToLong() {
    //given
    Policy policy = createPolicy();
    Step step = addStep(policy, FIRST_STEP_ID);
    UpdateStepRequest request = UpdateStepRequest.of(
        policy.getId(),
        step.getStepId(),
        STEP_NAME_THAT_EXCEEDED_MAX_LENGTH,
        random(100),
        SOLUTION_NO_DECISION,
        random(10));
    //when
    underTest.updateStep(request);
    Throwable thrown = catchThrowable(() -> {
      //This force hibernate to execute updates;
      entityManager.flush();
    });

    //then
    assertThat(thrown).isInstanceOf(PersistenceException.class);
    assertThat(thrown.getCause().getCause().getMessage()).isEqualTo(STEP_NAME_TOO_LONG_MSG);
  }

  @Test
  void shouldCloneFirstStepAndSetOrder() {
    //given
    Policy policy = createPolicy();
    Step firstStep = addStep(policy, FIRST_STEP_ID);
    UUID firstStepId = firstStep.getStepId();
    underTest.setStepsOrder(createSetStepsOrderRequest(POLICY_ID, of(firstStepId)));

    Step secondStep = addStep(policy, SECOND_STEP_ID);
    UUID secondStepId = secondStep.getStepId();
    underTest.setStepsOrder(createSetStepsOrderRequest(POLICY_ID, of(firstStepId, secondStepId)));

    //when
    CloneStepRequest cloneStepRequest =
        createCloneStepRequest(policy.getPolicyId(), firstStepId, OTHER_STEP_ID);

    UUID clonedStepId = underTest.cloneStep(cloneStepRequest);

    //then
    Integer firstStepOrder = stepRepository.getStepByStepId(firstStepId).getSortOrder();
    Integer clonedStepOrder = stepRepository.getStepByStepId(clonedStepId).getSortOrder();
    Integer thirdStepOrder = stepRepository.getStepByStepId(secondStepId).getSortOrder();

    assertThat(firstStep.getSortOrder()).isEqualTo(firstStepOrder);
    assertThat(clonedStepOrder).isEqualTo(firstStepOrder + 1);
    assertThat(thirdStepOrder).isEqualTo(firstStepOrder + 2);
  }

  @Test
  void shouldCloneLastStepAndSetOrder() {
    //given
    Policy policy = createPolicy();
    Step firstStep = addStep(policy, FIRST_STEP_ID);
    UUID firstStepId = firstStep.getStepId();
    underTest.setStepsOrder(createSetStepsOrderRequest(POLICY_ID, of(firstStepId)));

    Step secondStep = addStep(policy, SECOND_STEP_ID);
    UUID secondStepId = secondStep.getStepId();
    underTest.setStepsOrder(createSetStepsOrderRequest(POLICY_ID, of(firstStepId, secondStepId)));

    //when
    CloneStepRequest cloneStepRequest =
        createCloneStepRequest(policy.getPolicyId(), secondStepId, OTHER_STEP_ID);

    UUID clonedStepId = underTest.cloneStep(cloneStepRequest);

    //then
    Integer firstStepOrder = stepRepository.getStepByStepId(firstStepId).getSortOrder();
    Integer secondStepOrder = stepRepository.getStepByStepId(secondStepId).getSortOrder();
    Integer clonedStepOrder = stepRepository.getStepByStepId(clonedStepId).getSortOrder();

    assertThat(firstStep.getSortOrder()).isEqualTo(firstStepOrder);
    assertThat(secondStep.getSortOrder()).isEqualTo(secondStepOrder);
    assertThat(clonedStepOrder).isEqualTo(secondStepOrder + 1);
  }

  private Policy createPolicy() {
    Policy policy = new Policy(POLICY_ID, POLICY_NAME, POLICY_DESCRIPTION, USER);
    return policyRepository.save(policy);
  }

  private Step addStep(Policy policy, UUID stepId) {
    CreateStepRequest request = CreateStepRequest.of(
        policy.getPolicyId(),
        SOLUTION_NO_DECISION,
        stepId,
        STEP_NAME,
        STEP_DESCRIPTION,
        BUSINESS_LOGIC,
        USER);
    underTest.addStepToPolicy(request);
    return stepRepository.getStepByStepId(stepId);
  }

  private SetStepsOrderRequest createSetStepsOrderRequest(UUID policyID, List<UUID> steps) {
    return SetStepsOrderRequest.of(policyID, steps, OTHER_USER);
  }

  private CloneStepRequest createCloneStepRequest(UUID policyId, UUID baseStepId, UUID newStepID) {
    return CloneStepRequest.of(newStepID, baseStepId, policyId, OTHER_USER);
  }
}
