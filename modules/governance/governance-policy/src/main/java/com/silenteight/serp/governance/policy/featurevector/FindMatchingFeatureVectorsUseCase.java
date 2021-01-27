package com.silenteight.serp.governance.policy.featurevector;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.analytics.featurevector.FeatureVectorService;
import com.silenteight.serp.governance.analytics.featurevector.dto.FeatureVectorDto;
import com.silenteight.serp.governance.policy.domain.dto.StepConfigurationDto;
import com.silenteight.serp.governance.policy.featurevector.dto.FeatureVectorsDto;
import com.silenteight.serp.governance.policy.solve.ReconfigurableStepsConfigurationFactory;
import com.silenteight.serp.governance.policy.solve.SolvingService;
import com.silenteight.serp.governance.policy.solve.StepsConfigurationSupplier;
import com.silenteight.serp.governance.policy.solve.dto.SolveResponse;
import com.silenteight.serp.governance.policy.step.PolicyStepsConfigurationQuery;
import com.silenteight.serp.governance.policy.step.PolicyStepsRequestQuery;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

@RequiredArgsConstructor
public class FindMatchingFeatureVectorsUseCase {

  @NonNull
  private final PolicyStepsRequestQuery stepQuery;
  @NonNull
  private final PolicyStepsConfigurationQuery stepsConfigurationQuery;
  @NonNull
  private final ReconfigurableStepsConfigurationFactory stepsConfigurationFactory;
  @NonNull
  private final FeatureVectorService featureVectorService;
  @NonNull
  private final SolvingService solvingService;

  public FeatureVectorsDto activate(@NonNull UUID stepId) {
    StepsConfigurationSupplier stepsConfigurationProvider = getStepsConfigurationProvider(stepId);
    try (Stream<FeatureVectorDto> vectors = featureVectorService.getFeatureVectorStream()) {
      return FeatureVectorsDto.builder()
          .featureVectors(
              vectors
                  .map(FindMatchingFeatureVectorsUseCase::toFeatureValuesByName)
                  .filter(vector -> isSolvedWithStep(vector, stepsConfigurationProvider, stepId))
                  .map(FindMatchingFeatureVectorsUseCase::asDto)
                  .collect(toList()))
          .build();
    }
  }

  private static Map<String, String> toFeatureValuesByName(FeatureVectorDto vector) {
    List<String> names = vector.getNames();
    List<String> values = vector.getValues();

    return range(0, names.size())
        .boxed()
        .collect(toMap(names::get, values::get));
  }

  private StepsConfigurationSupplier getStepsConfigurationProvider(UUID stepId) {
    Long policyId = stepQuery.getPolicyIdForStep(stepId);
    List<StepConfigurationDto> stepsConfigurationDto = stepsConfigurationQuery
        .listStepsConfiguration(policyId);
    return stepsConfigurationFactory.getStepsConfigurationProvider(stepsConfigurationDto);
  }

  private boolean isSolvedWithStep(
      Map<String, String> featureValuesByName,
      StepsConfigurationSupplier stepsConfigurationProvider,
      UUID stepId) {

    SolveResponse solveResponse = solvingService.solve(
        stepsConfigurationProvider, featureValuesByName);
    UUID solvedStepId = solveResponse.getStepId();

    return nonNull(solvedStepId) && solvedStepId.equals(stepId);
  }

  private static com.silenteight.serp.governance.policy.featurevector.dto.FeatureVectorDto asDto(
      Map<String, String> featureValuesByName) {

    return com.silenteight.serp.governance.policy.featurevector.dto.FeatureVectorDto.builder()
        .featureValues(featureValuesByName)
        .build();
  }
}
