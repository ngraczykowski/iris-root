package com.silenteight.serp.governance.step.solve;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.BRANCH_FALSE_POSITIVE;
import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.BRANCH_NO_DECISION;
import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.BRANCH_POTENTIAL_TRUE_POSITIVE;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StepServiceTest {

  private static final String DEFAULT_STEP_NAME = "default";
  private static final SolutionWithStepId DEFAULT_SOLUTION =
      new SolutionWithStepId(DEFAULT_STEP_NAME, BRANCH_NO_DECISION);

  @Mock
  private StepPolicyFactory stepPolicyFactory;

  @InjectMocks
  private StepService underTest;

  @Test
  void matchedSolutionReturnsCorrectly() {
    // given
    List<String> values = List.of("PERFECT_MATCH", "DIGIT_MATCH");
    List<Step> steps = defaultStepsConfiguration();
    when(stepPolicyFactory.getSteps()).thenReturn(steps);

    // when
    SolutionWithStepId solution = underTest.getSolution(values);

    // then
    assertThat(solution).isEqualTo(
        new SolutionWithStepId("step_B", BRANCH_POTENTIAL_TRUE_POSITIVE));
  }

  @Test
  void defaultSolutionReturnsWhenNoStepMatchesValues() {
    // given
    List<String> values = List.of("OUT_OF_RANGE");
    List<Step> steps = defaultStepsConfiguration();
    when(stepPolicyFactory.getSteps()).thenReturn(steps);

    // when
    SolutionWithStepId solution = underTest.getSolution(values);

    // then
    assertThat(solution).isEqualTo(DEFAULT_SOLUTION);
  }

  private static List<Step> defaultStepsConfiguration() {
    return List.of(
        new Step(
            new SolutionWithStepId("step_A", BRANCH_FALSE_POSITIVE),
            Map.of(0, List.of(
                "EXACT_MATCH", "STRONG_MATCH", "WEAK_MATCH", "MATCH", "INCONCLUSIVE", "NO_DATA"))),
        new Step(
            new SolutionWithStepId("step_B", BRANCH_POTENTIAL_TRUE_POSITIVE),
            Map.of(1, List.of("PERFECT_MATCH", "DIGIT_MATCH"))));
  }
}
