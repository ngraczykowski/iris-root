package com.silenteight.serp.governance.policy.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.policy.domain.dto.StepDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.silenteight.proto.governance.v1.api.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.proto.governance.v1.api.FeatureVectorSolution.SOLUTION_NO_DECISION;
import static org.assertj.core.api.Assertions.*;

@TestPropertySource("classpath:data-test.properties")
@ContextConfiguration(classes = { PolicyRepositoryTestConfiguration.class })
class StepQueryTest extends BaseDataJpaTest {

  private static final UUID FIRST_POLICY_UID = UUID.randomUUID();
  private static final String FIRST_POLICY_NAME = "POLICY_1";
  private static final String FIRST_POLICY_CREATED_BY = "USER_1";

  private static final UUID FIRST_STEP_ID = UUID.randomUUID();
  private static final String FIRST_STEP_NAME = "FIRST_STEP_NAME";
  private static final String FIRST_STEP_DESC = "FIRST_STEP_DESC";
  private static final StepType FIRST_STEP_TYPE = StepType.MANUAL_RULE;

  private static final UUID SECOND_STEP_ID = UUID.randomUUID();
  private static final String SECOND_STEP_NAME = "SECOND_STEP_NAME";
  private static final String SECOND_STEP_DESC = "SECOND_STEP_DESC";
  private static final StepType SECOND_STEP_TYPE = StepType.AI_EXCEPTION;

  private StepQuery underTest;

  @Autowired
  private PolicyService policyService;

  @Autowired
  private StepRepository stepRepository;

  @Autowired
  private PolicyRepository policyRepository;


  @BeforeEach
  void setUp() {
    underTest = new PolicyDomainConfiguration().stepQuery(stepRepository, policyRepository);
  }

  @Test
  void listStepsOrderShouldReturnEmpty_whenNoStepsInPolicy() {
    policyService.addPolicy(FIRST_POLICY_UID, FIRST_POLICY_NAME, FIRST_POLICY_CREATED_BY);
    List<UUID> result = underTest.listStepsOrder(FIRST_POLICY_UID);

    assertThat(result).isEmpty();
  }

  @Test
  void listStepsShouldReturnEmpty_whenNoStepsInPolicy() {
    policyService.addPolicy(FIRST_POLICY_UID, FIRST_POLICY_NAME, FIRST_POLICY_CREATED_BY);

    Collection<StepDto> result = underTest.listSteps(FIRST_POLICY_UID);

    assertThat(result).isEmpty();
  }

  @Test
  void listStepsOrderShouldSteps_whenStepsInPolicy() {
    policyService.addPolicy(
        FIRST_POLICY_UID, FIRST_POLICY_NAME, FIRST_POLICY_CREATED_BY);
    policyService.addStepToPolicy(
        FIRST_POLICY_UID,
        SOLUTION_NO_DECISION,
        FIRST_STEP_ID,
        FIRST_STEP_NAME,
        FIRST_STEP_DESC,
        FIRST_STEP_TYPE);
    policyService.addStepToPolicy(
        FIRST_POLICY_UID,
        SOLUTION_FALSE_POSITIVE,
        SECOND_STEP_ID,
        SECOND_STEP_NAME,
        SECOND_STEP_DESC,
        SECOND_STEP_TYPE);

    List<UUID> result = underTest.listStepsOrder(FIRST_POLICY_UID);

    assertThat(result).contains(FIRST_STEP_ID, SECOND_STEP_ID);
  }

  @Test
  void listStepsShouldReturnSteps_whenStepsInPolicy() {
    policyService.addPolicy(
        FIRST_POLICY_UID, FIRST_POLICY_NAME, FIRST_POLICY_CREATED_BY);
    policyService.addStepToPolicy(
        FIRST_POLICY_UID,
        SOLUTION_NO_DECISION,
        FIRST_STEP_ID,
        FIRST_STEP_NAME,
        FIRST_STEP_DESC,
        FIRST_STEP_TYPE);
    policyService.addStepToPolicy(
        FIRST_POLICY_UID,
        SOLUTION_FALSE_POSITIVE,
        SECOND_STEP_ID,
        SECOND_STEP_NAME,
        SECOND_STEP_DESC,
        SECOND_STEP_TYPE);

    Collection<StepDto> result = underTest.listSteps(FIRST_POLICY_UID);

    assertThat(result).contains(
        StepDto.builder()
               .id(FIRST_STEP_ID)
               .solution(SOLUTION_NO_DECISION.name())
               .name(FIRST_STEP_NAME)
               .description(FIRST_STEP_DESC)
               .type(FIRST_STEP_TYPE.name())
               .build(),
        StepDto.builder()
               .id(SECOND_STEP_ID)
               .solution(SOLUTION_FALSE_POSITIVE.name())
               .name(SECOND_STEP_NAME)
               .description(SECOND_STEP_DESC)
               .type(SECOND_STEP_TYPE.name())
               .build());
  }
}
