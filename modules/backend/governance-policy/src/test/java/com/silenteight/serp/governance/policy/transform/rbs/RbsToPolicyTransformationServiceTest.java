package com.silenteight.serp.governance.policy.transform.rbs;

import com.silenteight.sep.base.testing.BaseDataJpaTest;
import com.silenteight.serp.governance.policy.details.PolicyDetailsQuery;
import com.silenteight.serp.governance.policy.domain.Condition;
import com.silenteight.serp.governance.policy.domain.PolicyService;
import com.silenteight.serp.governance.policy.domain.PolicyState;
import com.silenteight.serp.governance.policy.domain.StepType;
import com.silenteight.serp.governance.policy.domain.dto.*;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;
import com.silenteight.serp.governance.policy.step.logic.list.FeatureLogicRequestQuery;
import com.silenteight.serp.governance.policy.transform.rbs.StepsData.Feature;
import com.silenteight.serp.governance.policy.transform.rbs.StepsData.Step;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

import static com.silenteight.serp.governance.policy.domain.StepType.BUSINESS_LOGIC;
import static java.util.List.of;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@Transactional
@TestPropertySource("classpath:data-test.properties")
@ContextConfiguration(classes = { RbsToPolicyTransformationTestConfiguration.class })
class RbsToPolicyTransformationServiceTest extends BaseDataJpaTest {

  private static final String POLICY_RESOURCE_NAME_PREFIX = "policies/";
  private static final String POLICY_NAME = "TEST_POLICY";
  private static final String CREATED_BY = "StepsData";
  private static final PolicyState POLICY_STATE = PolicyState.SAVED;
  private static final Solution STEP_SOLUTION = Solution.POTENTIAL_TRUE_POSITIVE;
  private static final StepType STEP_TYPE = BUSINESS_LOGIC;
  private static final String STEP_RESOURCE_NAME_PREFIX = "steps/";
  private static final String REASONING_BRANCH_ID = "1";
  private static final String FEATURE_NAME = "features/name";
  private static final String FEATURE_VALUE = "NO_MATCH";

  private RbsToPolicyTransformationService underTest;

  @Autowired
  private PolicyService policyService;

  @Autowired
  private PolicyDetailsQuery policyDetailsQuery;

  @Autowired
  private PolicyStepsRequestQuery policyStepsRequestQuery;

  @Autowired
  private FeatureLogicRequestQuery featureLogicRequestQuery;

  @BeforeEach
  void setUp() {
    underTest = new RbsToPolicyTransformationService(policyService, policyStepsRequestQuery);
  }

  @Test
  void policyShouldBeCreated() {
    //given
    StepsData stepsData = createStepsData();

    //when
    var policyId = underTest.transform(stepsData);

    //then
    PolicyDto policyDto = policyDetailsQuery.details(policyId);

    assertEquals(POLICY_RESOURCE_NAME_PREFIX + policyId, policyDto.getName());
    assertEquals(POLICY_NAME, policyDto.getPolicyName());
    assertEquals(POLICY_STATE, policyDto.getState());
    assertEquals(CREATED_BY, policyDto.getCreatedBy());
    assertNotNull(policyDto.getCreatedAt());

    Collection<StepDto> stepDtos = policyStepsRequestQuery.listSteps(policyId);
    assertEquals(1, stepDtos.size());
    StepDto firstStep = stepDtos.stream().findFirst().orElse(null);
    assertEquals(STEP_SOLUTION, firstStep.getSolution());
    assertEquals(STEP_TYPE, firstStep.getType());
    assertEquals(CREATED_BY, firstStep.getCreatedBy());
    assertEquals(STEP_RESOURCE_NAME_PREFIX + firstStep.getId(), firstStep.getName());
    assertEquals(REASONING_BRANCH_ID, firstStep.getDescription());
    assertNotNull(firstStep.getCreatedAt());

    FeaturesLogicDto featuresLogicDto =
        featureLogicRequestQuery.listStepsFeaturesLogic(firstStep.getId());
    assertEquals(1, featuresLogicDto.getFeaturesLogic().size());
    FeatureLogicDto firstFeatureLogicDto =
        featuresLogicDto.getFeaturesLogic().stream().findFirst().orElse(null);
    assertThat(firstFeatureLogicDto).isEqualTo(FeatureLogicDto
        .builder()
        .toFulfill(1)
        .features(of(MatchConditionDto
            .builder()
            .condition(Condition.IS)
            .name(FEATURE_NAME)
            .values(of(FEATURE_VALUE))
            .build()))
        .build());
  }

  private StepsData createStepsData() {
    Feature feature = new Feature(FEATURE_NAME, FEATURE_VALUE);

    Step step = Step
        .builder()
        .solution(STEP_SOLUTION)
        .reasoningBranchId(REASONING_BRANCH_ID)
        .features(of(feature))
        .build();

    return StepsData.builder().name(POLICY_NAME).steps(of(step)).build();
  }
}
