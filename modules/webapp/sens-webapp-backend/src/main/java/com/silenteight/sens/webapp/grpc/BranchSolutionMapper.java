package com.silenteight.sens.webapp.grpc;

import com.silenteight.proto.serp.v1.recommendation.BranchSolution;

public class BranchSolutionMapper {

  private static final String SOLUTION_PREFIX = "BRANCH_";

  public BranchSolution map(String solution) {
    return BranchSolution.valueOf(addBranchPrefix(solution));
  }

  private static String addBranchPrefix(String newSolution) {
    return SOLUTION_PREFIX + newSolution.toUpperCase().replace(" ", "_");
  }

  public String map(BranchSolution solution) {
    return solution.name().replaceFirst(SOLUTION_PREFIX, "");
  }
}
