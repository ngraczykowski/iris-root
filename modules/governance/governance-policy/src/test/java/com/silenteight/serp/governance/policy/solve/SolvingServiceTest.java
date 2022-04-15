package com.silenteight.serp.governance.policy.solve;

import com.silenteight.serp.governance.policy.domain.Condition;
import com.silenteight.serp.governance.policy.solve.dto.SolveResponse;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static com.silenteight.serp.governance.policy.domain.Condition.IS_NOT;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_NO_DECISION;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_POTENTIAL_TRUE_POSITIVE;
import static java.util.List.of;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SolvingServiceTest {

  private static final SolveResponse DEFAULT_SOLUTION = new SolveResponse(SOLUTION_NO_DECISION);
  private static final UUID POLICY_ID = fromString("1f9b8139-9791-1ce1-0b58-4e08de1afe98");
  private static final UUID STEP_ID_1 = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
  private static final String STEP_TITLE_1 = "First step";
  private static final UUID STEP_ID_2 = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");
  private static final String STEP_TITLE_2 = "Second step";
  private static final List<Step> STEPS_WITH_IS_CONDITION = createStepsWithIsCondition();
  private static final List<Step> STEPS_WITH_IS_NOT_CONDITION = createStepsWithIsNotCondition();
  private static final List<Step> STEPS_WITH_IS_CONDITION_AND_DUPLICATED_FEATURES = List.of(
      createStepWithIsConditionAndDuplicatedFeatures());

  @InjectMocks
  private SolvingService underTest;

  @Mock
  private StepsSupplier stepsConfigurationSupplier;

  @Test
  void defaultSolutionReturnsWhenGivenOnlyOneFeatureValue() {
    // given
    when(stepsConfigurationSupplier.get()).thenReturn(STEPS_WITH_IS_CONDITION);
    Map<String, String> featureValues = Map.of(
        "features/nameAgent", "NEAR_MATCH");

    // when
    SolveResponse solution = underTest.solve(stepsConfigurationSupplier, featureValues);

    // then
    assertThat(solution).isEqualTo(DEFAULT_SOLUTION);
  }

  @Test
  void matchedSolutionReturnsCorrectly() {
    // given
    when(stepsConfigurationSupplier.get()).thenReturn(STEPS_WITH_IS_CONDITION);
    Map<String, String> featureValues = Map.of(
        "features/nameAgent", "NEAR_MATCH",
        "features/dateAgent", "EXACT",
        "features/documentAgent", "NO_DATA");


    List<String> expectedFeatures = List.of("features/nameAgent", "features/documentAgent",
        "features/dateAgent");
    // when
    SolveResponse solution = underTest.solve(stepsConfigurationSupplier, featureValues);
    // then
    assertThat(solution).isEqualTo(
        new SolveResponse(
            SOLUTION_POTENTIAL_TRUE_POSITIVE, expectedFeatures, of(), STEP_ID_2, STEP_TITLE_2));
  }

  @Test
  void matchedSolutionReturnsWhenGivenMoreFeatureValues() {
    // given
    when(stepsConfigurationSupplier.get()).thenReturn(STEPS_WITH_IS_CONDITION);
    Map<String, String> featureValues = Map.of(
        "features/nameAgent", "NEAR_MATCH",
        "features/dateAgent", "EXACT",
        "features/documentAgent", "NO_DATA",
        "anotherAgent", "EXACT");

    List<String> expectedFeatures = of("features/nameAgent", "features/documentAgent",
        "features/dateAgent");
    // when
    SolveResponse solution = underTest.solve(stepsConfigurationSupplier, featureValues);

    // then
    assertThat(solution).isEqualTo(
        new SolveResponse(
            SOLUTION_POTENTIAL_TRUE_POSITIVE, expectedFeatures, of(), STEP_ID_2, STEP_TITLE_2));
  }

  @Test
  void matchedSolutionReturnsWhenMoreFeatureLogicsMatchThanExpected() {
    // given
    when(stepsConfigurationSupplier.get()).thenReturn(STEPS_WITH_IS_CONDITION);
    Map<String, String> featureValues = Map.of(
        "features/nameAgent", "NEAR_MATCH",
        "features/dateAgent", "EXACT",
        "features/documentAgent", "DIGIT_MATCH");

    // when
    SolveResponse solution = underTest.solve(stepsConfigurationSupplier, featureValues);

    List<String> expectedFeatures = of("features/nameAgent", "features/documentAgent",
        "features/dateAgent");
    // then
    assertThat(solution).isEqualTo(
        new SolveResponse(
            SOLUTION_POTENTIAL_TRUE_POSITIVE, expectedFeatures, of(), STEP_ID_2, STEP_TITLE_2));
  }

  @Test
  void defaultSolutionReturnsWhenFeatureValuesDoNotMatch() {
    // given
    when(stepsConfigurationSupplier.get()).thenReturn(STEPS_WITH_IS_CONDITION);
    Map<String, String> featureValues = Map.of(
        "features/nameAgent", "EXACT",
        "features/dateAgent", "MATCH",
        "features/documentAgent", "NO_DATA");

    // when
    SolveResponse solution = underTest.solve(stepsConfigurationSupplier, featureValues);

    // then
    assertThat(solution).isEqualTo(DEFAULT_SOLUTION);
  }

  @Test
  void defaultSolutionReturnsWhenNoStepMatches() {
    // given
    when(stepsConfigurationSupplier.get()).thenReturn(STEPS_WITH_IS_CONDITION);
    Map<String, String> featureValues = Map.of(
        "features/nameAgent", "OUT_OF_RANGE",
        "features/dateAgent", "NO_DATA",
        "features/documentAgent", "MATCH");

    // when
    SolveResponse solution = underTest.solve(stepsConfigurationSupplier, featureValues);

    // then
    assertThat(solution).isEqualTo(DEFAULT_SOLUTION);
  }

  @Test
  void returnPotentialTruePositiveWhenTwoFeatureMatchIsNotCondition() {
    // given
    when(stepsConfigurationSupplier.get()).thenReturn(STEPS_WITH_IS_NOT_CONDITION);
    Map<String, String> featureValues = Map.of(
        "features/nameAgent", "MATCH",
        "features/dateAgent", "MATCH",
        "features/documentAgent", "MATCH");

    List<String> expectedFeatures = of("features/nameAgent", "features/dateAgent");

    // when
    SolveResponse solution = underTest.solve(stepsConfigurationSupplier, featureValues);

    // then
    assertThat(solution).isEqualTo(
        new SolveResponse(
            SOLUTION_POTENTIAL_TRUE_POSITIVE, expectedFeatures, of(), STEP_ID_1, STEP_TITLE_1));
  }

  @Test
  void defaultSolutionWhenOneFeatureMatchIsNotCondition() {
    // given
    when(stepsConfigurationSupplier.get()).thenReturn(STEPS_WITH_IS_NOT_CONDITION);
    Map<String, String> featureValues = Map.of(
        "features/nameAgent", "MATCH",
        "features/dateAgent", "NO_DATA",
        "features/documentAgent", "MATCH");

    // when
    SolveResponse solution = underTest.solve(stepsConfigurationSupplier, featureValues);

    // then
    assertThat(solution).isEqualTo(DEFAULT_SOLUTION);
  }

  private static List<Step> createStepsWithIsCondition() {
    return List.of(
        new Step(
            SOLUTION_FALSE_POSITIVE,
            STEP_ID_1,
            STEP_TITLE_1,
            List.of(
                createFeatureLogic(
                    2,
                    List.of(
                        createFeature("features/nameAgent", IS,
                            List.of("WEAK_MATCH", "MO_MATCH")),
                        createFeature("features/dateAgent", IS, List.of("WEAK")))))),
        new Step(
            SOLUTION_POTENTIAL_TRUE_POSITIVE,
            STEP_ID_2,
            STEP_TITLE_2,
            List.of(
                createFeatureLogic(
                    2,
                    List.of(
                        createFeature("features/nameAgent", IS,
                            List.of("PERFECT_MATCH", "NEAR_MATCH")),
                        createFeature("features/dateAgent", IS, List.of("EXACT")),
                        createFeature("features/documentAgent", IS,
                            List.of("MATCH", "DIGIT_MATCH")))))));
  }

  private static List<Step> createStepsWithIsNotCondition() {
    return List.of(
        new Step(
            SOLUTION_POTENTIAL_TRUE_POSITIVE,
            STEP_ID_1,
            STEP_TITLE_1,
            List.of(
                createFeatureLogic(
                    2,
                    List.of(
                        createFeature("features/nameAgent", IS_NOT,
                            List.of("NO_DATA", "INCONCLUSIVE")),
                        createFeature("features/dateAgent", IS_NOT,
                            List.of("NO_DATA", "INCONCLUSIVE"))))))
    );
  }

  private static Step createStepWithIsConditionAndDuplicatedFeatures() {
    return new Step(
        SOLUTION_POTENTIAL_TRUE_POSITIVE,
        STEP_ID_2,
        STEP_TITLE_2,
          List.of(
              createFeatureLogic(
                  2,
                  List.of(
                      createFeature("features/nameAgent", IS,
                          List.of("PERFECT_MATCH", "NEAR_MATCH")),
                      createFeature("categories/individual", IS, List.of("EXACT")),
                      createFeature("features/dateAgent", IS, List.of("EXACT")))),
              createFeatureLogic(
                  1,
                  List.of(
                      createFeature("features/nameAgent", IS,
                          List.of("NEAR_MATCH", "EXACT")),
                      createFeature("categories/individual", IS, List.of("EXACT")),
                      createFeature("features/dateAgent", IS, List.of("EXACT"))))
        ));
  }

  private static FeatureLogic createFeatureLogic(int count, Collection<MatchCondition> features) {
    return new FeatureLogic(count, features);
  }

  private static MatchCondition createFeature(
      String name, Condition condition, Collection<String> values) {

    return new MatchCondition(name, condition, values);
  }

  @Test
  void matchedSolutionReturnsCorrectlyWithoutDuplicates() {
    // given
    when(stepsConfigurationSupplier.get())
        .thenReturn(STEPS_WITH_IS_CONDITION_AND_DUPLICATED_FEATURES);

    Map<String, String> featureValues = Map.of(
        "features/nameAgent", "NEAR_MATCH",
        "categories/individual", "EXACT",
        "features/dateAgent", "EXACT");

    List<String> expectedFeatures = of("features/nameAgent", "features/dateAgent");
    List<String> expectedCategories = of("categories/individual");
    // when
    SolveResponse solution = underTest.solve(stepsConfigurationSupplier, featureValues);
    // then
    assertThat(solution).isEqualTo(
        new SolveResponse(SOLUTION_POTENTIAL_TRUE_POSITIVE,
            expectedFeatures,
            expectedCategories,
            STEP_ID_2,
            STEP_TITLE_2));
  }
}
