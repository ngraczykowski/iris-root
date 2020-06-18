package com.silenteight.sens.webapp.grpc;

import org.junit.jupiter.api.Test;

import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.BRANCH_FALSE_POSITIVE;
import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.BRANCH_HINTED_FALSE_POSITIVE;
import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.BRANCH_NO_DECISION;
import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.BRANCH_POTENTIAL_TRUE_POSITIVE;
import static org.assertj.core.api.Assertions.*;

class BranchSolutionMapperTest {

  @Test
  void mapsBranchSolutionToStringRemovingPrefix() {
    assertThat(BranchSolutionMapper.map(BRANCH_FALSE_POSITIVE)).isEqualTo("FALSE_POSITIVE");
    assertThat(BranchSolutionMapper.map(BRANCH_HINTED_FALSE_POSITIVE))
        .isEqualTo("HINTED_FALSE_POSITIVE");
    assertThat(BranchSolutionMapper.map(BRANCH_POTENTIAL_TRUE_POSITIVE))
        .isEqualTo("POTENTIAL_TRUE_POSITIVE");
    assertThat(BranchSolutionMapper.map(BRANCH_NO_DECISION)).isEqualTo("NO_DECISION");
  }
}
