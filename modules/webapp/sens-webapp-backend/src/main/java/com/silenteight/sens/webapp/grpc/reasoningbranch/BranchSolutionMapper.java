package com.silenteight.sens.webapp.grpc.reasoningbranch;

import com.silenteight.proto.serp.v1.recommendation.BranchSolution;

class BranchSolutionMapper {

  private static final String SOLUTION_PREFIX = "BRANCH_";

  BranchSolution map(String solution) {
    return BranchSolution.valueOf(addBranchPrefix(solution));
  }

  String map(BranchSolution solution) {
    return solution.name().replaceFirst(SOLUTION_PREFIX, "");
  }

  private static String addBranchPrefix(String newSolution) {
    return SOLUTION_PREFIX + newSolution.toUpperCase().replace(" ", "_");
  }
}
