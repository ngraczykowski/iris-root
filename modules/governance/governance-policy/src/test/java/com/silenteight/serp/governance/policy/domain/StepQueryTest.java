package com.silenteight.serp.governance.policy.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.policy.domain.dto.FeatureLogicConfigurationDto;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest.FeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ImportPolicyRequest.FeatureLogicConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.MatchConditionConfigurationDto;
import com.silenteight.serp.governance.policy.domain.dto.StepConfigurationDto;
import com.silenteight.serp.governance.policy.domain.dto.StepDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.silenteight.governance.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.governance.api.v1.FeatureVectorSolution.SOLUTION_NO_DECISION;
import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static com.silenteight.serp.governance.policy.domain.dto.Solution.of;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;

@TestPropertySource("classpath:data-test.properties")
@ContextConfiguration(classes = { PolicyRepositoryTestConfiguration.class })
class StepQueryTest extends BaseDataJpaTest {

  private static final UUID POLICY_UID = randomUUID();
  private static final String POLICY_NAME = "POLICY_1";
  private static final String POLICY_DESC = "FIRST_POLICY_DESCRIPTION";
  private static final String POLICY_CREATED_BY = "USER_1";

  private static final UUID FIRST_STEP_ID = randomUUID();
  private static final String FIRST_STEP_NAME = "FIRST_STEP_NAME";
  private static final String FIRST_STEP_DESC = "FIRST_STEP_DESC";
  private static final StepType FIRST_STEP_TYPE = StepType.MANUAL_RULE;

  private static final UUID SECOND_STEP_ID = randomUUID();
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
    policyService.addPolicy(
        POLICY_UID, POLICY_NAME, POLICY_DESC, POLICY_CREATED_BY);
    List<UUID> result = underTest.listStepsOrder(POLICY_UID);

    assertThat(result).isEmpty();
  }

  @Test
  void listStepsShouldReturnEmpty_whenNoStepsInPolicy() {
    policyService.addPolicy(
        POLICY_UID, POLICY_NAME, POLICY_DESC, POLICY_CREATED_BY);

    Collection<StepDto> result = underTest.listSteps(POLICY_UID);

    assertThat(result).isEmpty();
  }

  @Test
  void listStepsOrderShouldSteps_whenStepsInPolicy() {
    policyService.addPolicy(
        POLICY_UID, POLICY_NAME, POLICY_DESC, POLICY_CREATED_BY);
    policyService.addStepToPolicy(
        POLICY_UID,
        SOLUTION_NO_DECISION,
        FIRST_STEP_ID,
        FIRST_STEP_NAME,
        FIRST_STEP_DESC,
        FIRST_STEP_TYPE,
        1);
    policyService.addStepToPolicy(
        POLICY_UID,
        SOLUTION_FALSE_POSITIVE,
        SECOND_STEP_ID,
        SECOND_STEP_NAME,
        SECOND_STEP_DESC,
        SECOND_STEP_TYPE,
        0);

    List<UUID> result = underTest.listStepsOrder(POLICY_UID);

    assertThat(result).contains(FIRST_STEP_ID, SECOND_STEP_ID);
  }

  @Test
  void listStepsShouldReturnSteps_whenStepsInPolicy() {
    policyService.addPolicy(
        POLICY_UID, POLICY_NAME, POLICY_DESC, POLICY_CREATED_BY);
    policyService.addStepToPolicy(
        POLICY_UID,
        SOLUTION_NO_DECISION,
        FIRST_STEP_ID,
        FIRST_STEP_NAME,
        FIRST_STEP_DESC,
        FIRST_STEP_TYPE,
        0);
    policyService.addStepToPolicy(
        POLICY_UID,
        SOLUTION_FALSE_POSITIVE,
        SECOND_STEP_ID,
        SECOND_STEP_NAME,
        SECOND_STEP_DESC,
        SECOND_STEP_TYPE,
        1);

    Collection<StepDto> result = underTest.listSteps(POLICY_UID);

    assertThat(result).contains(
        StepDto.builder()
               .id(FIRST_STEP_ID)
               .solution(of(SOLUTION_NO_DECISION))
               .name(FIRST_STEP_NAME)
               .description(FIRST_STEP_DESC)
               .type(FIRST_STEP_TYPE)
               .build(),
        StepDto.builder()
               .id(SECOND_STEP_ID)
               .solution(of(SOLUTION_FALSE_POSITIVE))
               .name(SECOND_STEP_NAME)
               .description(SECOND_STEP_DESC)
               .type(SECOND_STEP_TYPE)
               .build());
  }

  @Test
  void listConfigurationStepsShouldReturnConfigurationSteps_whenStepsInPolicy() {
    // given
    createConfiguredPolicy();

    // when
    List<StepConfigurationDto> result = underTest.listStepsConfiguration(POLICY_UID);

    // then
    assertThat(result).contains(
        StepConfigurationDto.builder()
            .id(FIRST_STEP_ID)
            .solution(SOLUTION_NO_DECISION)
            .featureLogics(
                List.of(
                    createFeatureLogic(
                        1,
                        List.of(
                            createFeatureConfigurationDto(
                                "nameAgent", IS, List.of("MATCH", "NEAR_MATCH"))))))
            .build(),
        StepConfigurationDto.builder()
            .id(SECOND_STEP_ID)
            .solution(SOLUTION_FALSE_POSITIVE)
            .build());
  }

  @Test
  void getPolicyIdForStep_whenStepsInPolicy() {
    // given
    Long createdPolicyId = createConfiguredPolicy();

    // when
    Long policyId = underTest.getPolicyIdForStep(FIRST_STEP_ID);

    // then
    assertThat(policyId).isEqualTo(createdPolicyId);
  }

  private Long createConfiguredPolicy() {
    Policy policy = policyService.addPolicy(
        POLICY_UID, POLICY_NAME, POLICY_DESC, POLICY_CREATED_BY);
    policyService.addStepToPolicy(
        POLICY_UID,
        SOLUTION_NO_DECISION,
        FIRST_STEP_ID,
        FIRST_STEP_NAME,
        FIRST_STEP_DESC,
        FIRST_STEP_TYPE,
        0);
    policyService.addStepToPolicy(
        POLICY_UID,
        SOLUTION_FALSE_POSITIVE,
        SECOND_STEP_ID,
        SECOND_STEP_NAME,
        SECOND_STEP_DESC,
        SECOND_STEP_TYPE,
        1);
    FeatureConfiguration featureConfiguration = createFeatureConfiguration(
        "nameAgent", IS, List.of("MATCH", "NEAR_MATCH"));
    FeatureLogicConfiguration featureLogicConfiguration = FeatureLogicConfiguration
        .builder().count(1).featureConfigurations(List.of(featureConfiguration)).build();
    policyService.configureStepLogic(
        POLICY_UID,
        FIRST_STEP_ID,
        List.of(featureLogicConfiguration));

    return policy.getId();
  }

  private static FeatureLogicConfigurationDto createFeatureLogic(
      int count, Collection<MatchConditionConfigurationDto> features) {

    return FeatureLogicConfigurationDto.builder()
        .count(count)
        .features(features)
        .build();
  }

  private static FeatureConfiguration createFeatureConfiguration(
      String name, Condition condition, Collection<String> values) {

    return FeatureConfiguration.builder()
        .name(name)
        .condition(condition)
        .values(values)
        .build();
  }

  private static MatchConditionConfigurationDto createFeatureConfigurationDto(
      String name, Condition condition, Collection<String> values) {

    return MatchConditionConfigurationDto
        .builder().name(name).condition(condition).values(values).build();
  }
}
