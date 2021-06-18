package com.silenteight.serp.governance.vector.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.common.web.rest.Paging;
import com.silenteight.serp.governance.policy.domain.PolicyByIdQuery;
import com.silenteight.serp.governance.policy.solve.SolvingService;
import com.silenteight.serp.governance.policy.solve.StepsSupplier;
import com.silenteight.serp.governance.policy.solve.StepsSupplierProvider;
import com.silenteight.serp.governance.policy.solve.dto.SolveResponse;
import com.silenteight.serp.governance.policy.step.list.PolicyStepsRequestQuery;
import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorWithUsageDto;
import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorsDto;
import com.silenteight.serp.governance.vector.domain.dto.FeatureVectorsDto.FeatureVectorDto;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.serp.governance.policy.common.StepResource.fromResourceName;
import static com.silenteight.serp.governance.policy.common.StepResource.toResourceName;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class FindFeatureVectorsSolvedByStepUseCase {

  @NonNull
  private final FeatureNamesQuery featureNamesQuery;
  @NonNull
  private final SolvingService solvingService;
  @NonNull
  private final FeatureVectorUsageQuery featureVectorUsageQuery;
  @NonNull
  private final PolicyStepsRequestQuery policyStepsRequestQuery;
  @NonNull
  private final PolicyByIdQuery policyByIdQuery;
  @NonNull
  private final StepsSupplierProvider stepsSupplierProvider;

  @Transactional(readOnly = true)
  public FeatureVectorsDto activate(@NonNull String stepName, @Valid @NonNull Paging paging) {
    List<String> columns = getColumns();
    List<FeatureVectorDto> featureVectors = getFeatureVectors(
        fromResourceName(stepName), columns, paging);

    return FeatureVectorsDto.builder()
        .columns(columns)
        .featureVectors(featureVectors)
        .build();
  }

  private List<String> getColumns() {
    return featureNamesQuery.getUniqueFeatureNames();
  }

  private List<FeatureVectorDto> getFeatureVectors(
      UUID stepId, List<String> columns, @Valid Paging paging) {

    Long idOfPolicy = policyStepsRequestQuery.getPolicyIdForStep(stepId);
    UUID policyId = policyByIdQuery.getPolicyIdById(idOfPolicy);
    StepsSupplier stepsSupplier = stepsSupplierProvider.getStepsSupplier(policyId);
    FeatureVectorSolver featureVectorSolver = new FeatureVectorSolver(stepsSupplier, stepId);

    return featureVectorUsageQuery
        .getAllWithUsage()
        .filter(featureVectorSolver::isSolvedWithStep)
        .map(vector -> vector.standardize(columns, toResourceName(stepId)))
        .skip(paging.getSkip())
        .limit(paging.getPageSize())
        .collect(toList());
  }

  @RequiredArgsConstructor
  private class FeatureVectorSolver {

    @NonNull
    private final StepsSupplier stepsSupplier;

    @NonNull
    private final UUID stepId;

    public boolean isSolvedWithStep(FeatureVectorWithUsageDto vector) {
      SolveResponse solveResponse = solvingService.solve(stepsSupplier, vector.toNameValueMap());
      UUID solvedStepId = solveResponse.getStepId();

      return nonNull(solvedStepId) && solvedStepId.equals(stepId);
    }
  }
}
