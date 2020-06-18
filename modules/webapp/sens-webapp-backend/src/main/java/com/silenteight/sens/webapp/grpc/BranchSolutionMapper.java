package com.silenteight.sens.webapp.grpc;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.proto.serp.v1.recommendation.BranchSolution;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BranchSolutionMapper {

  private static final String SOLUTION_PREFIX = "BRANCH_";
  private static final Pattern SOLUTION_PREFIX_PATTERN = Pattern.compile(SOLUTION_PREFIX);

  public static BranchSolution map(String solution) {
    return BranchSolution.valueOf(addBranchPrefix(solution));
  }

  private static String addBranchPrefix(String newSolution) {
    return SOLUTION_PREFIX + newSolution.toUpperCase().replace(" ", "_");
  }

  public static String map(BranchSolution solution) {
    return SOLUTION_PREFIX_PATTERN.matcher(solution.name()).replaceFirst("");
  }
}
