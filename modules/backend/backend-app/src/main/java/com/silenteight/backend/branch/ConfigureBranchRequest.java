/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.backend.branch;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.proto.serp.v1.recommendation.BranchSolution;

import java.util.UUID;
import javax.annotation.Nullable;

@Value
@Builder
public class ConfigureBranchRequest {

  long decisionTreeId;
  long featureVectorId;

  @Nullable
  BranchSolution solution;
  @Nullable
  Boolean enabled;
  // BS
  @NonNull
  UUID correlationId;
}
