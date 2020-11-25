package com.silenteight.serp.governance.branch;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.proto.serp.v1.recommendation.BranchSolution;

import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;

@Value
@Builder
public class ConfigureBranchRequest implements BranchChange {

  long decisionTreeId;
  long featureVectorId;

  @Nullable
  BranchSolution solution;
  @Nullable
  Boolean enabled;
  // BS
  @NonNull
  UUID correlationId;

  @Override
  public Optional<BranchSolution> getSolutionChange() {
    return Optional.ofNullable(solution);
  }

  @Override
  public Optional<Boolean> getEnabledChange() {
    return Optional.ofNullable(enabled);
  }
}
