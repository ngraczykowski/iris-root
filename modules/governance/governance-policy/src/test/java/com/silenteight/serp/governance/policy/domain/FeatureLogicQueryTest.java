package com.silenteight.serp.governance.policy.domain;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.policy.domain.dto.*;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureConfiguration;
import com.silenteight.serp.governance.policy.domain.dto.ConfigurePolicyRequest.FeatureLogicConfiguration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;

import java.util.List;
import java.util.UUID;

import static com.silenteight.governance.api.v1.FeatureVectorSolution.SOLUTION_NO_DECISION;
import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;

@TestPropertySource("classpath:data-test.properties")
@ContextConfiguration(classes = { PolicyRepositoryTestConfiguration.class })
class FeatureLogicQueryTest extends BaseDataJpaTest {

  private static final UUID POLICY_UID = UUID.randomUUID();
  private static final String POLICY_NAME = "POLICY_1";
  private static final String POLICY_DESC = "POLICY_DESC";
  private static final String POLICY_CREATED_BY = "USER_1";

  private static final UUID STEP_ID = UUID.randomUUID();
  private static final String STEP_NAME = "FIRST_STEP_NAME";
  private static final String STEP_DESC = "FIRST_STEP_DESC";
  private static final StepType STEP_TYPE = StepType.MANUAL_RULE;

  private static final String MATCH = "match";
  private static final String HQ_NO_MATCH = "hq_no_match";
  private static final String INDIVIDUAL = "I";
  private static final String FEATURE_NAME_1 = "apType";
  private static final String FEATURE_NAME_2 = "name";
  private static final String FEATURE_NAME_3 = "dob";
  private static final String FEATURE_NAME_4 = "doc";
  private static final String USER_NAME = "user";

  private FeaturesLogicQuery underTest;

  @Autowired
  private PolicyService policyService;

  @Autowired
  private PolicyRepository policyRepository;

  @Autowired
  private FeatureLogicRepository featureLogicRepository;

  @Autowired
  private StepRepository stepRepository;


  @BeforeEach
  void setUp() {
    underTest = new PolicyDomainConfiguration().featuresLogicQuery(
        stepRepository, featureLogicRepository);
  }

  @Test
  void listLogicShouldReturnEmpty_whenNothingIsSaved() {
    policyService.addPolicy(
        POLICY_UID, POLICY_NAME, POLICY_CREATED_BY);
    CreateStepRequest request = CreateStepRequest.of(
        POLICY_UID, SOLUTION_NO_DECISION, STEP_ID, STEP_NAME, STEP_DESC, STEP_TYPE, USER_NAME);
    policyService.addStepToPolicy(request);

    FeaturesLogicDto result = underTest.listStepsFeaturesLogic(STEP_ID);

    assertThat(result.getFeaturesLogic()).isEmpty();
  }

  @Test
  void listLogicShouldReturnLogic_whenLogicIsSaved() {
    UUID uuid = policyService.addPolicy(POLICY_UID, POLICY_NAME, POLICY_CREATED_BY);
    Policy policy = policyRepository.getByPolicyId(uuid);
    CreateStepRequest createStepRequest = CreateStepRequest.of(
        POLICY_UID, SOLUTION_NO_DECISION, STEP_ID, STEP_NAME, STEP_DESC, STEP_TYPE, "user");
    policyService.addStepToPolicy(createStepRequest);
    FeatureLogicConfiguration firstLogic = FeatureLogicConfiguration
        .builder()
        .toFulfill(1)
        .featureConfigurations(of(getFeatureConfiguration(FEATURE_NAME_1, of(INDIVIDUAL))))
        .build();
    FeatureLogicConfiguration secondLogic = FeatureLogicConfiguration
        .builder()
        .toFulfill(2)
        .featureConfigurations(of(
            getFeatureConfiguration(FEATURE_NAME_2, of(MATCH, HQ_NO_MATCH)),
            getFeatureConfiguration(FEATURE_NAME_3, of(MATCH)),
            getFeatureConfiguration(FEATURE_NAME_4, of(MATCH))))
        .build();

    ConfigureStepLogicRequest request = ConfigureStepLogicRequest
        .of(policy.getId(), STEP_ID, of(firstLogic, secondLogic), USER_NAME);
    policyService.configureStepLogic(request);

    FeaturesLogicDto result = underTest.listStepsFeaturesLogic(STEP_ID);

    assertThat(result).isEqualTo(
        FeaturesLogicDto
            .builder()
            .featuresLogic(of(
                getFeaturesDto(1, of(getFeatureDto(FEATURE_NAME_1, of(INDIVIDUAL)))),
                getFeaturesDto(2, of(
                    getFeatureDto(FEATURE_NAME_2, of(MATCH, HQ_NO_MATCH)),
                    getFeatureDto(FEATURE_NAME_3, of(MATCH)),
                    getFeatureDto(FEATURE_NAME_4, of(MATCH))
                ))))
            .build()
    );
  }

  private FeatureConfiguration getFeatureConfiguration(String featureName2, List<String> match) {
    return FeatureConfiguration.builder().name(featureName2).condition(IS).values(match).build();
  }

  private FeatureLogicDto getFeaturesDto(int count, List<MatchConditionDto> featureDtos) {
    return FeatureLogicDto.builder().toFulfill(count).features(featureDtos).build();
  }

  private MatchConditionDto getFeatureDto(String featureName, List<String> values) {
    return MatchConditionDto.builder().name(featureName).condition(IS).values(values).build();
  }
}
