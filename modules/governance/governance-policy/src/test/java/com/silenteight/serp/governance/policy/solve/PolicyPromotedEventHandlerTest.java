package com.silenteight.serp.governance.policy.solve;

import com.silenteight.serp.governance.policy.domain.Condition;
import com.silenteight.serp.governance.policy.domain.dto.FeatureLogicConfigurationDto;
import com.silenteight.serp.governance.policy.domain.dto.MatchConditionConfigurationDto;
import com.silenteight.serp.governance.policy.domain.dto.StepConfigurationDto;
import com.silenteight.serp.governance.policy.domain.events.NewPolicyInUseEvent;
import com.silenteight.serp.governance.policy.step.PolicyStepsConfigurationQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.UUID;

import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_POTENTIAL_TRUE_POSITIVE;
import static java.util.UUID.fromString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PolicyPromotedEventHandlerTest {

  private static final UUID CORRELATION_ID = UUID.randomUUID();
  @Mock
  private PolicyStepsConfigurationQuery stepsConfigurationQuery;

  @Mock
  private InUsePolicyQuery inUsePolicyQuery;

  private PolicyStepsSupplierFactory underTest;

  @BeforeEach
  void setUp() {
    SolveConfiguration configuration = new SolveConfiguration();
    this.underTest = configuration.stepsSupplierFactory(
        stepsConfigurationQuery, inUsePolicyQuery);
  }

  @Test
  void handlePolicyPromotedEvent() {
    // given
    UUID policyId = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
    List<StepConfigurationDto> steps = createSteps();
    NewPolicyInUseEvent event = NewPolicyInUseEvent
        .builder()
        .policyId(policyId)
        .correlationId(CORRELATION_ID)
        .build();
    when(stepsConfigurationQuery.listStepsConfiguration(policyId)).thenReturn(steps);

    // when
    underTest.handleNewPolicy(event);

    // then
    verify(stepsConfigurationQuery).listStepsConfiguration(policyId);
  }

  private static List<StepConfigurationDto> createSteps() {
    List<MatchConditionConfigurationDto> firstStepMatchConfiguration = List.of(
        getMatchConditionConfigurationDto("nameAgent", IS, List.of("PERFECT_MATCH", "NEAR_MATCH")),
        getMatchConditionConfigurationDto("dateAgent", IS, List.of("EXACT", "NEAR")));
    List<FeatureLogicConfigurationDto> firstStepFeaturesConfiguration = List.of(
        FeatureLogicConfigurationDto
            .builder()
            .count(2)
            .features(firstStepMatchConfiguration)
            .build());

    List<MatchConditionConfigurationDto> secondStepMatchConfiguration = List.of(
        getMatchConditionConfigurationDto("documentAgent", IS, List.of("MATCH", "DIGIT_MATCH")));
    List<FeatureLogicConfigurationDto> secondStepFeaturesConfiguration = List.of(
        FeatureLogicConfigurationDto
            .builder()
            .count(1)
            .features(secondStepMatchConfiguration)
            .build());

    return List.of(
        StepConfigurationDto.builder()
            .id(fromString("de1afe98-0b58-4941-9791-4e081f9b8139"))
            .solution(SOLUTION_FALSE_POSITIVE)
            .featureLogics(firstStepFeaturesConfiguration)
            .build(),
        StepConfigurationDto.builder()
            .id(fromString("de1afe98-0b58-4941-9791-4e081f9b8139"))
            .solution(SOLUTION_POTENTIAL_TRUE_POSITIVE)
            .featureLogics(secondStepFeaturesConfiguration)
            .build());
  }

  private static MatchConditionConfigurationDto getMatchConditionConfigurationDto(
      String name, Condition condition, List<String> values) {

    return MatchConditionConfigurationDto.builder()
        .name(name)
        .condition(condition)
        .values(values)
        .build();
  }
}
