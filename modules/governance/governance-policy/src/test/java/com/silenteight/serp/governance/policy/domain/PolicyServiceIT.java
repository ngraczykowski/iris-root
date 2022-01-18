package com.silenteight.serp.governance.policy.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.policy.domain.dto.CreateStepRequest;
import com.silenteight.serp.governance.policy.domain.dto.UpdateStepRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;
import javax.persistence.PersistenceException;

import static com.silenteight.serp.governance.policy.domain.DomainConstants.MAX_STEP_NAME_LENGTH;
import static com.silenteight.serp.governance.policy.domain.StepType.BUSINESS_LOGIC;
import static com.silenteight.serp.governance.policy.domain.TestFixtures.*;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_NO_DECISION;
import static java.lang.String.format;
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
    UUID stepUuid = UUID.randomUUID();
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
    UUID stepUuid = UUID.randomUUID();
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
    Step step = addStep(policy);
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
    Step step = addStep(policy);
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

  private Policy createPolicy() {
    Policy policy = new Policy(UUID.randomUUID(), POLICY_NAME, POLICY_DESCRIPTION, USER);
    return policyRepository.save(policy);
  }

  private Step addStep(Policy policy) {
    UUID stepUuid = UUID.randomUUID();
    CreateStepRequest request = CreateStepRequest.of(
        policy.getPolicyId(),
        SOLUTION_NO_DECISION,
        stepUuid,
        STEP_NAME,
        STEP_DESCRIPTION,
        BUSINESS_LOGIC,
        USER);
    underTest.addStepToPolicy(request);
    return stepRepository.getStepByStepId(stepUuid);
  }
}
