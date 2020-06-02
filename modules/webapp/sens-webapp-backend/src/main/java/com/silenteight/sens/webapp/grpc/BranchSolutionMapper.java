package com.silenteight.sens.webapp.grpc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.proto.serp.v1.recommendation.BranchSolution;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BranchSolutionMapper {

  private static final String SOLUTION_PREFIX = "BRANCH_";

  public static BranchSolution map(String solution) {
    return BranchSolution.valueOf(addBranchPrefix(solution));
  }

  private static String addBranchPrefix(String newSolution) {
    return SOLUTION_PREFIX + newSolution.toUpperCase().replace(" ", "_");
  }

  public static String map(BranchSolution solution) {
    return solution.name().replaceFirst(SOLUTION_PREFIX, "");
  }
}
