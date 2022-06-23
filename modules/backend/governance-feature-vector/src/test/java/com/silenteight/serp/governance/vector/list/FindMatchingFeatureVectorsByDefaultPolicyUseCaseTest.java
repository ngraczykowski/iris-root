package com.silenteight.serp.governance.vector.list;

import com.silenteight.serp.governance.common.web.rest.Paging;
import com.silenteight.serp.governance.policy.domain.Condition;
import com.silenteight.serp.governance.policy.domain.InUsePolicyQuery;
import com.silenteight.serp.governance.policy.domain.dto.FeatureLogicConfigurationDto;
import com.silenteight.serp.governance.policy.domain.dto.MatchConditionConfigurationDto;
import com.silenteight.serp.governance.policy.domain.dto.StepConfigurationDto;
import com.silenteight.serp.governance.policy.solve.DefaultStepsProvider;
import com.silenteight.serp.governance.policy.solve.SolvingService;
import com.silenteight.serp.governance.policy.solve.StepsSupplier;
import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorWithUsageDto;
import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorsDto;
import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorsDto.FeatureVectorDto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.silenteight.serp.governance.policy.domain.Condition.IS;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_FALSE_POSITIVE;
import static com.silenteight.solving.api.v1.FeatureVectorSolution.SOLUTION_POTENTIAL_TRUE_POSITIVE;
import static java.util.Optional.*;
import static java.util.UUID.fromString;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FindMatchingFeatureVectorsByDefaultPolicyUseCaseTest {

  private static final long ID_OF_POLICY = 11;
  private static final UUID POLICY_ID = fromString("306659cf-569d-4138-8a71-2ec0578653b1");
  private static final List<String> COLUMNS =
      List.of("dateAgent", "genderAgent", "nameAgent", "nationalityAgent");
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
  private static final String STEP_NAME_1 = "steps/" + STEP_ID_1;
  private static final String STEP_TITLE_1 = "First step";
  private static final UUID STEP_ID_2 = fromString("de1afe98-0b58-4941-9791-4e081f9b8139");
  private static final String STEP_NAME_2 = "steps/" + STEP_ID_2;
  private static final String STEP_TITLE_2 = "Second step";
  private static final UUID STEP_ID_3 = fromString("1f9b8139-9791-1ce1-0b58-4e08de1afe98");
  private static final String STEP_NAME_3 = "steps/" + STEP_ID_3;
  private static final String STEP_TITLE_3 = "Third step";
  private static final int PAGE_INDEX = 0;
  private static final int PAGE_SIZE = 10;
  private static final Paging PAGING = new Paging(PAGE_INDEX, PAGE_SIZE);

  @Mock
  private FeatureVectorUsageQuery featureVectorUsageQuery;
  @Mock
  private FeatureNamesQuery featureNamesQuery;
  @Mock
  private ListVectorsQuery listVectorsQuery;
  @Mock
  private InUsePolicyQuery inUsePolicyQuery;

  private FindFeatureVectorsSolvedByDefaultPolicyUseCase underTest;

  @BeforeEach
  void setUp() {
    List<StepConfigurationDto> steps = createSteps();
    StepsSupplier stepsConfigurationSupplier = new DefaultStepsProvider(steps);

    underTest = new FindFeatureVectorsSolvedByDefaultPolicyUseCase(
        featureNamesQuery,
        new SolvingService(),
        featureVectorUsageQuery,
        policyId -> stepsConfigurationSupplier,
        inUsePolicyQuery,
        listVectorsQuery);
  }

  @Test
  void whenPolicyIsAvailable_findMatchingFeatureVectors() {
    // given
    Stream<FeatureVectorWithUsageDto> featureVectors = createFeatureVectors();
    when(featureNamesQuery.getUniqueFeatureNames()).thenReturn(COLUMNS);
    when(inUsePolicyQuery.getPolicyInUse()).thenReturn(of(POLICY_ID));
    when(featureVectorUsageQuery.getAllWithUsage()).thenReturn(featureVectors);

    // when
    FeatureVectorsDto response = underTest.activate(PAGING);

    // then
    assertThat(response.getColumns()).isEqualTo(COLUMNS);
    assertThat(response.getFeatureVectors()).isNotEmpty();
    assertThat(response.getFeatureVectors())
        .extracting(FeatureVectorDto::getSignature)
        .containsExactly(SIGNATURE_1, SIGNATURE_2, SIGNATURE_3);
    assertThat(response.getFeatureVectors())
        .extracting(FeatureVectorDto::getMatchesCount)
        .containsExactly(USAGE_COUNT_1, USAGE_COUNT_2, USAGE_COUNT_3);
    assertThat(response.getFeatureVectors())
        .extracting(FeatureVectorDto::getStep)
        .containsExactly(STEP_NAME_1, null, null);
  }

  @Test
  void whenPolicyIsNotAvailable_findFeatureVectorsWithoutSteps() {
    // given
    List<FeatureVectorDto> featureVectors = createFeatureVectors()
        .map(featureVectorWithUsageDto -> featureVectorWithUsageDto.standardize(COLUMNS))
        .collect(Collectors.toList());
    FeatureVectorsDto featureVectorsDto = FeatureVectorsDto
        .builder()
        .featureVectors(featureVectors)
        .columns(COLUMNS).build();
    when(listVectorsQuery.list(any())).thenReturn(featureVectorsDto);
    when(inUsePolicyQuery.getPolicyInUse()).thenReturn(empty());

    // when
    FeatureVectorsDto response = underTest.activate(PAGING);

    // then
    assertThat(response.getColumns()).isEqualTo(COLUMNS);
    assertThat(response.getFeatureVectors()).isNotEmpty();
    assertThat(response.getFeatureVectors())
        .extracting(FeatureVectorDto::getSignature)
        .containsExactly(SIGNATURE_1, SIGNATURE_2, SIGNATURE_3);
    assertThat(response.getFeatureVectors())
        .extracting(FeatureVectorDto::getMatchesCount)
        .containsExactly(USAGE_COUNT_1, USAGE_COUNT_2, USAGE_COUNT_3);
    assertThat(response.getFeatureVectors())
        .extracting(FeatureVectorDto::getStep)
        .containsExactly(null, null, null);
  }

  private static List<StepConfigurationDto> createSteps() {
    return List.of(
        StepConfigurationDto.builder()
            .id(STEP_ID_1)
            .title(STEP_TITLE_1)
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
            .title(STEP_TITLE_2)
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
            .title(STEP_TITLE_3)
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
