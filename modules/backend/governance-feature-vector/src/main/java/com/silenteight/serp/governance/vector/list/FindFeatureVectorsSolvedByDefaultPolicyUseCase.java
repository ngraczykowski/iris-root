package com.silenteight.serp.governance.vector.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.common.web.rest.Paging;
import com.silenteight.serp.governance.policy.common.StepResource;
import com.silenteight.serp.governance.policy.domain.InUsePolicyQuery;
import com.silenteight.serp.governance.policy.solve.SolvingService;
import com.silenteight.serp.governance.policy.solve.StepsSupplier;
import com.silenteight.serp.governance.policy.solve.StepsSupplierProvider;
import com.silenteight.serp.governance.policy.solve.dto.SolveResponse;
import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorWithUsageDto;
import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorsDto;
import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorsDto.FeatureVectorDto;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class FindFeatureVectorsSolvedByDefaultPolicyUseCase {

  @NonNull
  private final FeatureNamesQuery featureNamesQuery;
  @NonNull
  private final SolvingService solvingService;
  @NonNull
  private final FeatureVectorUsageQuery featureVectorUsageQuery;
  @NonNull
  private final StepsSupplierProvider stepsSupplierProvider;
  @NonNull
  private final InUsePolicyQuery inUsePolicyQuery;
  @NonNull
  private final ListVectorsQuery listVectorsQuery;

  @Transactional(readOnly = true)
  public FeatureVectorsDto activate(@Valid @NonNull Paging paging) {
    Optional<UUID> policyInUse = inUsePolicyQuery.getPolicyInUse();
    if (policyInUse.isEmpty())
      return listVectorsQuery.list(paging);
    else
      return getFeatureVectorsBasedOnPolicy(policyInUse.get(), paging);
  }

  private List<String> getColumns() {
    return featureNamesQuery.getUniqueFeatureNames();
  }

  private FeatureVectorsDto getFeatureVectorsBasedOnPolicy(UUID policyId, Paging paging) {
    List<String> columns = getColumns();
    List<FeatureVectorDto> featureVectors = getFeatureVectors(policyId, columns, paging);
    return FeatureVectorsDto.builder().columns(columns).featureVectors(featureVectors).build();
  }

  private List<FeatureVectorDto> getFeatureVectors(
      UUID policyId, List<String> columns, @Valid Paging paging) {

    StepsSupplier stepsSupplier = stepsSupplierProvider.getStepsSupplier(policyId);
    return featureVectorUsageQuery
        .getAllWithUsage()
        .skip(paging.getSkip())
        .limit(paging.getPageSize())
        .map(vectorWithUsage -> new SolvedFeatureVector(vectorWithUsage, stepsSupplier, columns))
        .map(SolvedFeatureVector::toDto)
        .collect(toList());
  }

  @RequiredArgsConstructor
  private class SolvedFeatureVector {
    @NonNull
    private final FeatureVectorWithUsageDto vector;
    @NonNull
    private final StepsSupplier stepsSupplier;
    @NonNull
    private final List<String> columns;

    private FeatureVectorDto toDto() {
      SolveResponse solveResponse = solvingService.solve(stepsSupplier, vector.toNameValueMap());
      String stepName = ofNullable(solveResponse.getStepId())
          .map(StepResource::toResourceName)
          .orElse(null);
      return vector.standardize(columns, stepName);
    }
  }
}
