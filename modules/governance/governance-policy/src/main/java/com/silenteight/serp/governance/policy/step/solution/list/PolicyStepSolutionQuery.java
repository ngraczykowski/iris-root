package com.silenteight.serp.governance.policy.step.solution.list;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.recommendation.BranchSolution;

import java.util.List;
import java.util.Set;

import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.BRANCH_HINTED_FALSE_POSITIVE;
import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.BRANCH_HINTED_POTENTIAL_TRUE_POSITIVE;
import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.UNRECOGNIZED;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
class PolicyStepSolutionQuery {

  private static final Set<BranchSolution> HINTED_SOLUTIONS =
      Set.of(BRANCH_HINTED_FALSE_POSITIVE, BRANCH_HINTED_POTENTIAL_TRUE_POSITIVE);

  private final boolean hintedEnabled;

  List<String> list() {
    return stream(BranchSolution.values())
        .filter(PolicyStepSolutionQuery::isRecognized)
        .filter(this::isNotHintedIfHintedDisabled)
        .map(BranchSolutionMapper::map)
        .collect(toList());
  }

  private static boolean isRecognized(BranchSolution branchSolution) {
    return branchSolution != UNRECOGNIZED;
  }

  private boolean isNotHintedIfHintedDisabled(BranchSolution branchSolution) {
    return hintedEnabled || !HINTED_SOLUTIONS.contains(branchSolution);
  }
}
