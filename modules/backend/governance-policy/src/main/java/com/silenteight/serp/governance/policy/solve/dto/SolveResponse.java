package com.silenteight.serp.governance.policy.solve.dto;

import lombok.*;

import com.silenteight.solving.api.v1.FeatureVectorSolution;

import java.util.*;
import javax.annotation.Nullable;

import static com.silenteight.serp.governance.policy.common.StepResource.toResourceName;
import static java.util.Collections.emptyList;

@Data
@RequiredArgsConstructor
public class SolveResponse {

  public SolveResponse(@NonNull FeatureVectorSolution solution) {
    this.solution = solution;
    this.features = emptyList();
    this.categories = emptyList();
    this.stepId = null;
    this.stepTitle = null;
  }

  @NonNull
  private final FeatureVectorSolution solution;
  @NonNull
  private final List<String> features;
  @NonNull
  private final List<String> categories;
  @Nullable
  private final UUID stepId;
  @Nullable
  private final String stepTitle;

  public String getStepName() {
    if (stepId == null)
      return null;

    return toResourceName(stepId);
  }
}
