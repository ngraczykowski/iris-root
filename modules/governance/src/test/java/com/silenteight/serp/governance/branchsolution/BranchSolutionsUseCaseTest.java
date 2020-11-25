package com.silenteight.serp.governance.branchsolution;

import org.junit.jupiter.api.Test;

import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.*;
import static org.assertj.core.api.Assertions.*;

class BranchSolutionsUseCaseTest {

  @Test
  void returnsAvailableSolutionsIncludingHinted() {
    BranchSolutionUseCase useCase = new BranchSolutionUseCase(true);

    assertThat(useCase.listAvailableBranchSolutions().getBranchSolutionsList())
        .containsExactlyInAnyOrder(
            BRANCH_FALSE_POSITIVE,
            BRANCH_POTENTIAL_TRUE_POSITIVE,
            BRANCH_NO_DECISION,
            BRANCH_HINTED_FALSE_POSITIVE,
            BRANCH_HINTED_POTENTIAL_TRUE_POSITIVE);
  }

  @Test
  void returnsAvailableSolutionsExcludingHinted() {
    BranchSolutionUseCase useCase = new BranchSolutionUseCase(false);

    assertThat(useCase.listAvailableBranchSolutions().getBranchSolutionsList())
        .containsExactlyInAnyOrder(
            BRANCH_FALSE_POSITIVE,
            BRANCH_POTENTIAL_TRUE_POSITIVE,
            BRANCH_NO_DECISION);
  }
}
