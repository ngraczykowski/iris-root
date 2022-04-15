package com.silenteight.serp.governance.policy.step.edit;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.serp.governance.policy.domain.dto.Solution;
import com.silenteight.solving.api.v1.FeatureVectorSolution;

import java.util.UUID;
import javax.annotation.Nullable;

import static java.util.Optional.ofNullable;

@Builder
@Value
class EditStepCommand {

  @NonNull
  UUID id;
  @Nullable
  String name;
  @Nullable
  String description;
  @Nullable
  Solution solution;
  @NonNull
  String updatedBy;

  public FeatureVectorSolution getFeatureVectorSolution() {
    return ofNullable(getSolution()).map(Solution::getFeatureVectorSolution).orElse(null);
  }
}
