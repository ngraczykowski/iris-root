package com.silenteight.serp.governance.step.solve;

import lombok.Value;

import com.silenteight.proto.serp.v1.recommendation.BranchSolution;

@Value
public class SolutionWithStepId {

  String stepId;
  BranchSolution solution;
}
