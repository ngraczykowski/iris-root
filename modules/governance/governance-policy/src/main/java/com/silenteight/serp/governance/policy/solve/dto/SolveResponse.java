package com.silenteight.serp.governance.policy.solve.dto;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.governance.api.v1.FeatureVectorSolution;

import java.util.UUID;

@Data
@RequiredArgsConstructor
public class SolveResponse {

  public SolveResponse(FeatureVectorSolution solution) {
    this.solution = solution;
    this.stepId = null;
  }

  @NonNull
  private final FeatureVectorSolution solution;

  private final UUID stepId;
}
