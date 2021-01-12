package com.silenteight.serp.governance.policy.solve;

import com.silenteight.proto.governance.v1.api.GetSolutionsRequest;
import com.silenteight.proto.governance.v1.api.GetSolutionsResponse;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static com.silenteight.proto.governance.v1.api.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.proto.governance.v1.api.FeatureVectorSolution.SOLUTION_NO_DECISION;
import static com.silenteight.proto.governance.v1.api.FeatureVectorSolution.SOLUTION_POTENTIAL_TRUE_POSITIVE;
import static com.silenteight.serp.governance.GovernanceProtoUtils.featureCollection;
import static com.silenteight.serp.governance.GovernanceProtoUtils.featureVector;
import static com.silenteight.serp.governance.GovernanceProtoUtils.solutionsRequest;
import static com.silenteight.serp.governance.policy.solve.GetSolutionsResponseAssert.assertThat;
import static java.util.UUID.fromString;
import static org.mockito.Mockito.*;

class SolveUseCaseTest {

  private static final UUID STEP_ID_1 = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
  private static final UUID STEP_ID_2 = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");

  private final StepPolicyFactory stepPolicyFactory = Mockito.mock(StepPolicyFactory.class);
  private final SolveUseCase underTest = new SolveUseCase(new SolvingService(stepPolicyFactory));

  @Test
  void matchedSolutionReturnsCorrectly() {
    // given
    List<Step> steps = defaultStepsConfiguration();
    when(stepPolicyFactory.getSteps()).thenReturn(steps);
    GetSolutionsRequest solutionsRequest = solutionsRequest(
        featureCollection("nameAgent", "dateAgent"),
        featureVector("PERFECT_MATCH", "EXACT"));

    // when
    GetSolutionsResponse response = underTest.solve(solutionsRequest);

    // then
    assertThat(response)
        .hasSolutionsCount(1)
        .solution(0)
        .hasStepId(STEP_ID_2)
        .hasSolution(SOLUTION_POTENTIAL_TRUE_POSITIVE);
  }

  @Test
  void defaultSolutionReturnsWhenNoStepMatchesValues() {
    // given
    List<Step> steps = defaultStepsConfiguration();
    when(stepPolicyFactory.getSteps()).thenReturn(steps);
    GetSolutionsRequest solutionsRequest = solutionsRequest(
        featureCollection("nameAgent"),
        featureVector("OUT_OF_RANGE"));

    // when
    GetSolutionsResponse response = underTest.solve(solutionsRequest);

    // then
    assertThat(response)
        .hasSolutionsCount(1)
        .solution(0)
        .hasSolution(SOLUTION_NO_DECISION);
  }

  private static List<Step> defaultStepsConfiguration() {
    return List.of(
        new Step(
            SOLUTION_FALSE_POSITIVE,
            STEP_ID_1,
            List.of(
                createFeatureLogic(
                    2,
                    List.of(
                        createFeature("nameAgent", List.of("WEAK_MATCH", "MO_MATCH")),
                        createFeature("dateAgent", List.of("WEAK")))))),
        new Step(
            SOLUTION_POTENTIAL_TRUE_POSITIVE,
            STEP_ID_2,
            List.of(
                createFeatureLogic(
                    2,
                    List.of(
                        createFeature("nameAgent", List.of("PERFECT_MATCH", "NEAR_MATCH")),
                        createFeature("dateAgent", List.of("EXACT")),
                        createFeature("documentAgent", List.of("MATCH", "DIGIT_MATCH")))))));
  }

  private static FeatureLogic createFeatureLogic(int count, Collection<Feature> features) {
    return FeatureLogic.builder()
        .count(count)
        .features(features)
        .build();
  }

  private static Feature createFeature(String name, Collection<String> values) {
    return Feature.builder()
        .name(name)
        .values(values)
        .build();
  }
}
