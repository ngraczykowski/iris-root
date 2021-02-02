package com.silenteight.serp.governance.policy.featurevector;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.policy.featurevector.dto.FeatureVectorDto;
import com.silenteight.serp.governance.policy.featurevector.dto.FeatureVectorWithUsageDto;
import com.silenteight.serp.governance.policy.featurevector.dto.FeatureVectorsDto;
import com.silenteight.serp.governance.policy.solve.SolvingService;
import com.silenteight.serp.governance.policy.solve.StepsConfigurationSupplier;
import com.silenteight.serp.governance.policy.solve.dto.SolveResponse;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.IntStream.range;

@RequiredArgsConstructor
public class FindMatchingFeatureVectorsUseCase {

  @NonNull
  private final StepsConfigurationSupplierFactory configurationSupplier;
  @NonNull
  private final FeatureVectorUsageQuery featureVectorUsageQuery;
  @NonNull
  private final FeatureNamesQuery featureNamesQuery;
  @NonNull
  private final SolvingService solvingService;

  @Transactional
  public FeatureVectorsDto activate(@NonNull UUID stepId) {
    List<String> columns = getColumns();
    List<FeatureVectorDto> featureVectors = getFeatureVectors(stepId, columns);

    return FeatureVectorsDto.builder()
        .columns(columns)
        .featureVectors(featureVectors)
        .build();
  }

  private List<String> getColumns() {
    return featureNamesQuery
        .getUniqueFeatureNames()
        .stream()
        .sorted()
        .collect(toList());
  }

  private List<FeatureVectorDto> getFeatureVectors(UUID stepId, List<String> columns) {
    StepsConfigurationSupplier stepsConfigurationProvider =
        configurationSupplier.getConfigurationSupplierBasedOnStep(stepId);
    FeatureVectorSolver featureVectorSolver =
        new FeatureVectorSolver(stepsConfigurationProvider, stepId);

    return featureVectorUsageQuery
        .getAllWithUsage()
        .map(FeatureValueWithUsageWrapper::new)
        .filter(featureVectorSolver::isSolvedWithStep)
        .map(wrapper -> mapToDto(columns, wrapper))
        .collect(toList());
  }

  private static FeatureVectorDto mapToDto(
      List<String> columns, FeatureValueWithUsageWrapper wrapper) {

    return FeatureVectorDto.builder()
        .signature(wrapper.getSignature())
        .usageCount(wrapper.getUsageCount())
        .values(mapToValues(columns, wrapper.getFeatureValuesByName()))
        .build();
  }

  private static List<String> mapToValues(
      List<String> columns, Map<String, String> featureValuesByName) {

    return columns
        .stream()
        .map(featureValuesByName::get)
        .collect(toList());
  }

  private static class FeatureValueWithUsageWrapper {

    @NonNull
    private final FeatureVectorWithUsageDto featureVectorWithUsage;
    @NonNull
    private final Map<String, String> featureValuesByName;

    FeatureValueWithUsageWrapper(FeatureVectorWithUsageDto featureVectorWithUsage) {
      this.featureVectorWithUsage = featureVectorWithUsage;
      featureValuesByName = toFeatureValuesByName(featureVectorWithUsage);
    }

    private static Map<String, String> toFeatureValuesByName(FeatureVectorWithUsageDto vector) {
      List<String> names = vector.getNames();
      List<String> values = vector.getValues();

      return range(0, names.size())
          .boxed()
          .collect(toMap(names::get, values::get));
    }

    String getSignature() {
      return featureVectorWithUsage.getSignature();
    }

    long getUsageCount() {
      return featureVectorWithUsage.getUsageCount();
    }

    Map<String, String> getFeatureValuesByName() {
      return featureValuesByName;
    }
  }

  @RequiredArgsConstructor
  private class FeatureVectorSolver {

    @NonNull
    private final StepsConfigurationSupplier stepsConfigurationProvider;
    @NonNull
    private final UUID stepId;

    public boolean isSolvedWithStep(FeatureValueWithUsageWrapper wrapper) {
      SolveResponse solveResponse = solvingService.solve(
          stepsConfigurationProvider, wrapper.getFeatureValuesByName());
      UUID solvedStepId = solveResponse.getStepId();

      return nonNull(solvedStepId) && solvedStepId.equals(stepId);
    }
  }
}
