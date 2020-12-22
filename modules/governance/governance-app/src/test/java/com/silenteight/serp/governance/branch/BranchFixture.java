package com.silenteight.serp.governance.branch;

import lombok.Builder;
import lombok.Value;

import com.silenteight.proto.serp.v1.recommendation.BranchSolution;

import java.time.OffsetDateTime;

@Value
@Builder
public class BranchFixture {

  long decisionTreeId;
  long featureVectorId;
  BranchSolution solution;
  boolean enabled;
  OffsetDateTime lastUsedAt;
}
