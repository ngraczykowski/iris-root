package com.silenteight.serp.governance.policy.solve;

import com.silenteight.proto.governance.v1.api.GetSolutionsRequest;
import com.silenteight.proto.governance.v1.api.GetSolutionsResponse;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Map;

import static com.silenteight.proto.governance.v1.api.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.proto.governance.v1.api.FeatureVectorSolution.SOLUTION_NO_DECISION;
import static com.silenteight.proto.governance.v1.api.FeatureVectorSolution.SOLUTION_POTENTIAL_TRUE_POSITIVE;
import static com.silenteight.serp.governance.GovernanceProtoUtils.featureCollection;
import static com.silenteight.serp.governance.GovernanceProtoUtils.featureVector;
import static com.silenteight.serp.governance.GovernanceProtoUtils.solutionsRequest;
import static com.silenteight.serp.governance.policy.solve.GetSolutionsResponseAssert.assertThat;
import static org.mockito.Mockito.*;

class SolveUseCaseTest {

  private static final String DEFAULT_STEP_NAME = "default";

  private final StepPolicyFactory stepPolicyFactory = Mockito.mock(StepPolicyFactory.class);
  private final SolveUseCase underTest = new SolveUseCase(new SolvingService(stepPolicyFactory));

  @Test
  void matchedSolutionReturnsCorrectly() {
    // given
    List<Step> steps = defaultStepsConfiguration();
    when(stepPolicyFactory.getSteps()).thenReturn(steps);
    GetSolutionsRequest solutionsRequest = solutionsRequest(
        featureCollection("F1", "F2"),
        featureVector("PERFECT_MATCH", "DIGIT_MATCH")
    );

    // when
    GetSolutionsResponse response = underTest.solve(solutionsRequest);

    // then
    assertThat(response)
        .hasSolutionsCount(1)
        .solution(0)
          .hasStepId("step_B")
          .hasSolution(SOLUTION_POTENTIAL_TRUE_POSITIVE);
  }

  @Test
  void defaultSolutionReturnsWhenNoStepMatchesValues() {
    // given
    List<Step> steps = defaultStepsConfiguration();
    when(stepPolicyFactory.getSteps()).thenReturn(steps);
    GetSolutionsRequest solutionsRequest = solutionsRequest(
        featureCollection("F1"),
        featureVector("OUT_OF_RANGE")
    );

    // when
    GetSolutionsResponse response = underTest.solve(solutionsRequest);

    // then
    assertThat(response)
        .hasSolutionsCount(1)
        .solution(0)
          .hasStepId(DEFAULT_STEP_NAME)
          .hasSolution(SOLUTION_NO_DECISION);
  }

  private static List<Step> defaultStepsConfiguration() {
    return List.of(
        new Step(
            new SolutionWithStepId("step_A", SOLUTION_FALSE_POSITIVE),
            Map.of(0, List.of(
                "EXACT_MATCH", "STRONG_MATCH", "WEAK_MATCH", "MATCH", "INCONCLUSIVE", "NO_DATA"))),
        new Step(
            new SolutionWithStepId("step_B", SOLUTION_POTENTIAL_TRUE_POSITIVE),
            Map.of(1, List.of("PERFECT_MATCH", "DIGIT_MATCH"))));
  }
}
