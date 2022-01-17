package com.silenteight.serp.governance.policy.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.policy.domain.dto.*;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureLogicConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.StepSearchCriteriaDto.ConditionSearchCriteriaDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static com.silenteight.serp.governance.policy.domain.StepType.BUSINESS_LOGIC;
import static com.silenteight.serp.governance.policy.domain.StepType.NARROW;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_NO_DECISION;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static java.util.List.of;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.*;

@Transactional
@TestPropertySource("classpath:data-test.properties")
@ContextConfiguration(classes = { PolicyRepositoryTestConfiguration.class })
class StepQueryTest extends BaseDataJpaTest {

  private static final UUID POLICY_UID = randomUUID();
  private static final String POLICY_NAME = "POLICY_1";
  private static final String POLICY_CREATED_BY = "USER_1";

  private static final UUID FIRST_STEP_ID = randomUUID();
  private static final String FIRST_STEP_NAME = "FIRST_STEP_NAME";
  private static final String FIRST_STEP_DESC = "FIRST_STEP_DESC";
  private static final StepType FIRST_STEP_TYPE = BUSINESS_LOGIC;

  private static final UUID SECOND_STEP_ID = randomUUID();
  private static final String SECOND_STEP_NAME = "SECOND_STEP_NAME";
  private static final String SECOND_STEP_DESC = "SECOND_STEP_DESC";
  private static final StepType SECOND_STEP_TYPE = NARROW;

  private static final String USER = "user";

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
    policyService.createPolicy(
        POLICY_UID, POLICY_NAME, POLICY_CREATED_BY);
    List<UUID> result = underTest.listStepsOrder(POLICY_UID);

    assertThat(result).isEmpty();
  }

  @Test
  void listStepsShouldReturnEmpty_whenNoStepsInPolicy() {
    policyService.createPolicy(
        POLICY_UID, POLICY_NAME, POLICY_CREATED_BY);

    Collection<StepDto> result = underTest.listSteps(POLICY_UID);

    assertThat(result).isEmpty();
  }

  @Test
  void listStepsOrderShouldSteps_whenStepsInPolicy() {
    createPolicyWithTwoSteps();

    List<UUID> result = underTest.listStepsOrder(POLICY_UID);

    assertThat(result).contains(FIRST_STEP_ID, SECOND_STEP_ID);
  }

  @Test
  void listStepsShouldReturnSteps_whenStepsInPolicy() {
    createPolicyWithTwoSteps();

    Collection<StepDto> result = underTest.listSteps(POLICY_UID);

    assertThat(result).extracting(StepDto::getId).contains(FIRST_STEP_ID, SECOND_STEP_ID);
  }

  @Test
  void listFilteredStepsShouldReturnEmpty_whenStepsInPolicyNotMatchNameCriteria() {
    createConfiguredPolicy();
    Collection<StepDto> result = underTest.listFilteredSteps(POLICY_UID, new StepSearchCriteriaDto(
        of(new ConditionSearchCriteriaDto("nonMatched", asList("NEAR_MATCH", "MATCH")))));

    assertThat(result).isEmpty();
  }

  @Test
  void listFilteredStepsShouldReturnEmpty_whenStepsInPolicyNotMatchValuesCriteria() {
    createConfiguredPolicy();
    Collection<StepDto> result = underTest.listFilteredSteps(POLICY_UID, new StepSearchCriteriaDto(
        of(new ConditionSearchCriteriaDto("nameAgent", singletonList("NO_DATA")))));

    assertThat(result).isEmpty();
  }

  @Test
  void listFilteredStepsShouldReturnEmpty_whenStepsInPolicyPartiallyMatchValuesCriteria() {
    createConfiguredPolicy();
    Collection<StepDto> result = underTest.listFilteredSteps(POLICY_UID, new StepSearchCriteriaDto(
        of(new ConditionSearchCriteriaDto("nameAgent", asList("NO_DATA", "MATCH")))));

    assertThat(result).isEmpty();
  }

  @Test
  void listFilteredStepsShouldReturnEmpty_whenStepsInPolicyMatchByContainsCriteria() {
    createConfiguredPolicy();
    Collection<StepDto> result = underTest.listFilteredSteps(POLICY_UID, new StepSearchCriteriaDto(
        of(new ConditionSearchCriteriaDto("nameAgent", singletonList("MATCH")))));

    assertThat(result).extracting(StepDto::getId).contains(FIRST_STEP_ID);
  }

  @Test
  void listFilteredStepsShouldReturnSteps_whenStepsInPolicyMatchByEqualCriteria() {
    createConfiguredPolicy();
    Collection<StepDto> result = underTest.listFilteredSteps(POLICY_UID, new StepSearchCriteriaDto(
        of(new ConditionSearchCriteriaDto("nameAgent", asList("MATCH", "NEAR_MATCH")))));

    assertThat(result).extracting(StepDto::getId).contains(FIRST_STEP_ID);
  }

  @Test
  void listConfigurationStepsShouldReturnConfigurationSteps_whenStepsInPolicy() {
    // given
    createConfiguredPolicy();

    // when
    List<StepConfigurationDto> result = underTest.listStepsConfiguration(POLICY_UID);

    // then
    List<MatchConditionConfigurationDto> featureConfigurationDto = of(
        createFeatureConfigurationDto("nameAgent", IS, of("MATCH", "NEAR_MATCH")));
    StepConfigurationDto firstStep = StepConfigurationDto
        .builder()
        .id(FIRST_STEP_ID)
        .title(FIRST_STEP_NAME)
        .solution(SOLUTION_NO_DECISION)
        .featureLogics(of(createFeatureLogic(1, featureConfigurationDto)))
        .build();
    StepConfigurationDto secondStep = StepConfigurationDto
        .builder()
        .id(SECOND_STEP_ID)
        .title(SECOND_STEP_NAME)
        .solution(SOLUTION_FALSE_POSITIVE)
        .build();
    assertThat(result).contains(firstStep, secondStep);
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

  @Test
  void getStepsCount_whenNoStepsInPolicy() {
    // given
    policyService.createPolicy(POLICY_UID, POLICY_NAME, POLICY_CREATED_BY);

    // when
    long stepsCount = underTest.getStepsCount(POLICY_UID);

    // then
    assertThat(stepsCount).isZero();
  }

  @Test
  void getStepsCount_whenStepsInPolicy() {
    // given
    createPolicyWithTwoSteps();

    // when
    long stepsCount = underTest.getStepsCount(POLICY_UID);

    // then
    assertThat(stepsCount).isEqualTo(2L);
  }

  private Long createConfiguredPolicy() {
    createPolicyWithTwoSteps();
    Long policyId = policyRepository.getIdByPolicyId(POLICY_UID);

    FeatureConfiguration featureConfiguration = createFeatureConfiguration(
        "nameAgent", IS, of("MATCH", "NEAR_MATCH"));
    FeatureLogicConfiguration featureLogicConfiguration = FeatureLogicConfiguration
        .builder().toFulfill(1).featureConfigurations(of(featureConfiguration)).build();
    ConfigureStepLogicRequest request = ConfigureStepLogicRequest
        .of(policyId, FIRST_STEP_ID, of(featureLogicConfiguration), USER);
    policyService.configureStepLogic(request);

    return policyId;
  }

  private static FeatureLogicConfigurationDto createFeatureLogic(
      int count, Collection<MatchConditionConfigurationDto> features) {

    return FeatureLogicConfigurationDto
        .builder()
        .count(count)
        .features(features)
        .build();
  }

  private static FeatureConfiguration createFeatureConfiguration(
      String name, Condition condition, Collection<String> values) {

    return FeatureConfiguration
        .builder()
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

  private void createPolicyWithTwoSteps() {
    policyService.createPolicy(POLICY_UID, POLICY_NAME, POLICY_CREATED_BY);
    CreateStepRequest firstRequest = CreateStepRequest.of(
        POLICY_UID,
        SOLUTION_NO_DECISION,
        FIRST_STEP_ID,
        FIRST_STEP_NAME,
        FIRST_STEP_DESC,
        FIRST_STEP_TYPE,
        POLICY_CREATED_BY);
    policyService.addStepToPolicy(firstRequest);
    CreateStepRequest secondRequest = CreateStepRequest.of(
        POLICY_UID,
        SOLUTION_FALSE_POSITIVE,
        SECOND_STEP_ID,
        SECOND_STEP_NAME,
        SECOND_STEP_DESC,
        SECOND_STEP_TYPE,
        POLICY_CREATED_BY);
    policyService.addStepToPolicy(secondRequest);
    SetStepsOrderRequest stepsOrderReqest = SetStepsOrderRequest.of(
        POLICY_UID, of(SECOND_STEP_ID, FIRST_STEP_ID), USER);
    policyService.setStepsOrder(stepsOrderReqest);
  }
}
