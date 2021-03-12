package com.silenteight.serp.governance.policy.featurevector;

import com.silenteight.serp.governance.policy.domain.Condition;
import com.silenteight.serp.governance.policy.domain.dto.FeatureLogicConfigurationDto;
import com.silenteight.serp.governance.policy.domain.dto.MatchConditionConfigurationDto;
import com.silenteight.serp.governance.policy.domain.dto.StepConfigurationDto;
import com.silenteight.serp.governance.policy.featurevector.dto.FeatureVectorDto;
import com.silenteight.serp.governance.policy.featurevector.dto.FeatureVectorWithUsageDto;
import com.silenteight.serp.governance.policy.featurevector.dto.FeatureVectorsDto;
import com.silenteight.serp.governance.policy.solve.DefaultStepsProvider;
import com.silenteight.serp.governance.policy.solve.SolvingService;
import com.silenteight.serp.governance.policy.solve.StepsSupplier;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_POTENTIAL_TRUE_POSITIVE;
import static java.util.Arrays.asList;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindMatchingFeatureVectorsUseCaseTest {

  private static final long ID_OF_POLICY = 11;
  private static final UUID POLICY_ID = fromString("306659cf-569d-4138-8a71-2ec0578653b1");
  private static final List<String> COLUMNS =
      List.of("nameAgent", "dateAgent", "genderAgent", "nationalityAgent");
  private static final String SIGNATURE_1 = "signature_1";
  private static final long USAGE_COUNT_1 = 2L;
  private static final List<String> FEATURE_NAMES_1 =
      List.of("nameAgent", "dateAgent", "nationalityAgent");
  private static final List<String> FEATURE_VALUES_1 = List.of("PERFECT_MATCH", "EXACT", "MATCH");
  private static final String SIGNATURE_2 = "signature_2";
  private static final long USAGE_COUNT_2 = 5L;
  private static final List<String> FEATURE_NAMES_2 = List.of("documentAgent");
  private static final List<String> FEATURE_VALUES_2 = List.of("DIGIT_MATCH");
  private static final String SIGNATURE_3 = "signature_3";
  private static final long USAGE_COUNT_3 = 6L;
  private static final List<String> FEATURE_NAMES_3 = List.of("documentAgent");
  private static final List<String> FEATURE_VALUES_3 = List.of("NEAR_MATCH");
  private static final UUID STEP_ID_1 = fromString("01256804-1ce1-4d52-94d4-d1876910f272");
  private static final UUID STEP_ID_2 = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");
  private static final UUID STEP_ID_3 = fromString("1f9b8139-9791-1ce1-0b58-4e08de1afe98");

  @Mock
  private FeatureVectorUsageQuery featureVectorUsageQuery;
  @Mock
  private FeatureNamesQuery featureNamesQuery;
  @Mock
  private PolicyStepsRequestQuery policyStepsRequestQuery;
  @Mock
  private PolicyByIdQuery policyByIdQuery;

  private FindMatchingFeatureVectorsUseCase underTest;

  @BeforeEach
  void setUp() {
    List<StepConfigurationDto> steps = createSteps();
    StepsSupplier stepsConfigurationSupplier = new DefaultStepsProvider(steps);

    underTest = new FindMatchingFeatureVectorsUseCase(
        featureNamesQuery,
        new SolvingService(),
        featureVectorUsageQuery,
        policyStepsRequestQuery,
        policyByIdQuery,
        policyId -> stepsConfigurationSupplier);
  }

  @Test
  void whenStepIsUsed_findMatchingFeatureVectors() {
    // given
    Stream<FeatureVectorWithUsageDto> featureVectors = createFeatureVectors();
    when(featureNamesQuery.getUniqueFeatureNames()).thenReturn(COLUMNS);
    when(policyStepsRequestQuery.getPolicyIdForStep(STEP_ID_1)).thenReturn(ID_OF_POLICY);
    when(policyByIdQuery.getPolicyIdById(ID_OF_POLICY)).thenReturn(POLICY_ID);
    when(featureVectorUsageQuery.getAllWithUsage()).thenReturn(featureVectors);

    // when
    FeatureVectorsDto response = underTest.activate(STEP_ID_1);

    // then
    assertThat(response.getColumns())
        .isEqualTo(List.of("dateAgent", "genderAgent", "nameAgent", "nationalityAgent"));
    assertThat(response.getFeatureVectors()).isNotEmpty();
    assertThat(response.getFeatureVectors())
        .extracting(FeatureVectorDto::getSignature)
        .containsExactly(SIGNATURE_1);
    assertThat(response.getFeatureVectors())
        .extracting(FeatureVectorDto::getUsageCount)
        .containsExactly(USAGE_COUNT_1);
    assertThat(response.getFeatureVectors())
        .extracting(FeatureVectorDto::getValues)
        .containsExactly(asList("EXACT", null, "PERFECT_MATCH", "MATCH"));
  }

  @Test
  void whenStepIsNotUsed_findNoMatchingFeatureVectors() {
    // given
    Stream<FeatureVectorWithUsageDto> featureVectors = createFeatureVectors();
    when(featureVectorUsageQuery.getAllWithUsage()).thenReturn(featureVectors);
    when(policyStepsRequestQuery.getPolicyIdForStep(STEP_ID_2)).thenReturn(ID_OF_POLICY);
    when(policyByIdQuery.getPolicyIdById(ID_OF_POLICY)).thenReturn(POLICY_ID);

    // when
    FeatureVectorsDto response = underTest.activate(STEP_ID_2);

    // then
    assertThat(response.getFeatureVectors()).isEmpty();
  }

  @Test
  void whenPreviousStepIsUsed_findNoMatchingFeatureVectors() {
    // given
    Stream<FeatureVectorWithUsageDto> featureVectors = createFeatureVectors();
    when(featureVectorUsageQuery.getAllWithUsage()).thenReturn(featureVectors);
    when(policyStepsRequestQuery.getPolicyIdForStep(STEP_ID_3)).thenReturn(ID_OF_POLICY);
    when(policyByIdQuery.getPolicyIdById(ID_OF_POLICY)).thenReturn(POLICY_ID);

    // when
    FeatureVectorsDto response = underTest.activate(STEP_ID_3);

    // then
    assertThat(response.getFeatureVectors()).isEmpty();
  }

  private static List<StepConfigurationDto> createSteps() {
    return List.of(
        StepConfigurationDto.builder()
            .id(STEP_ID_1)
            .solution(SOLUTION_POTENTIAL_TRUE_POSITIVE)
            .featureLogics(
                List.of(
                    createFeatureLogic(
                        2,
                        List.of(
                            createMatchConditionConfigurationDto(
                                "nameAgent", IS, List.of("PERFECT_MATCH", "NEAR_MATCH")),
                            createMatchConditionConfigurationDto(
                                "dateAgent", IS, List.of("EXACT", "NEAR"))))))
            .build(),
        StepConfigurationDto.builder()
            .id(STEP_ID_2)
            .solution(SOLUTION_FALSE_POSITIVE)
            .featureLogics(
                List.of(
                    createFeatureLogic(
                        1,
                        List.of(
                            createMatchConditionConfigurationDto(
                                "documentAgent", IS, List.of("NO_MATCH", "NO_DATA"))))))
            .build(),
        StepConfigurationDto.builder()
            .id(STEP_ID_3)
            .solution(SOLUTION_POTENTIAL_TRUE_POSITIVE)
            .featureLogics(
                List.of(
                    createFeatureLogic(
                        1,
                        List.of(
                            createMatchConditionConfigurationDto(
                                "nationalityAgent", IS, List.of("MATCH"))))))
            .build());
  }

  private static FeatureLogicConfigurationDto createFeatureLogic(
      int count, Collection<MatchConditionConfigurationDto> features) {

    return FeatureLogicConfigurationDto.builder()
        .count(count)
        .features(features)
        .build();
  }

  private static MatchConditionConfigurationDto createMatchConditionConfigurationDto(
      String name, Condition condition, List<String> values) {

    return MatchConditionConfigurationDto.builder()
        .name(name)
        .condition(condition)
        .values(values)
        .build();
  }

  private static Stream<FeatureVectorWithUsageDto> createFeatureVectors() {
    return Stream.of(
        FeatureVectorWithUsageDto.builder()
            .signature(SIGNATURE_1)
            .usageCount(USAGE_COUNT_1)
            .names(FEATURE_NAMES_1)
            .values(FEATURE_VALUES_1)
            .build(),
        FeatureVectorWithUsageDto.builder()
            .signature(SIGNATURE_2)
            .usageCount(USAGE_COUNT_2)
            .names(FEATURE_NAMES_2)
            .values(FEATURE_VALUES_2)
            .build(),
        FeatureVectorWithUsageDto.builder()
            .signature(SIGNATURE_3)
            .usageCount(USAGE_COUNT_3)
            .names(FEATURE_NAMES_3)
            .values(FEATURE_VALUES_3)
            .build());
  }
}
