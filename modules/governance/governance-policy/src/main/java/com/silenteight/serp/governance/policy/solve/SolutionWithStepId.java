package com.silenteight.serp.governance.policy.solve;

import lombok.Value;

import com.silenteight.proto.governance.v1.api.FeatureVectorSolution;

@Value
public class SolutionWithStepId {

  String stepId;
  FeatureVectorSolution solution;
}
