package com.silenteight.serp.governance.policy.step.solution.list;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.proto.serp.v1.recommendation.BranchSolution;

import java.util.regex.Pattern;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class BranchSolutionMapper {

  private static final String SOLUTION_PREFIX = "BRANCH_";
  private static final Pattern SOLUTION_PREFIX_PATTERN = Pattern.compile(SOLUTION_PREFIX);

  public static String map(BranchSolution solution) {
    return SOLUTION_PREFIX_PATTERN.matcher(solution.name()).replaceFirst("");
  }
}
