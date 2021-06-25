package com.silenteight.serp.governance.policy.solve.dto;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.solving.api.v1.FeatureVectorSolution;

import java.util.UUID;
import javax.annotation.Nullable;

import static com.silenteight.serp.governance.policy.common.StepResource.toResourceName;

@Data
@RequiredArgsConstructor
public class SolveResponse {

  public SolveResponse(FeatureVectorSolution solution) {
    this.solution = solution;
    this.stepId = null;
  }

  @NonNull
  private final FeatureVectorSolution solution;
  @Nullable
  private final UUID stepId;

  public String getStepName() {
    return toResourceName(stepId);
  }
}
