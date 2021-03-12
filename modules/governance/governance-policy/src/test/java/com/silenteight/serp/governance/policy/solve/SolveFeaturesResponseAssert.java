package com.silenteight.serp.governance.policy.solve;

import com.silenteight.solving.api.v1.BatchSolveFeaturesResponse;

import org.assertj.core.api.AbstractAssert;

public class SolveFeaturesResponseAssert extends
    AbstractAssert<SolveFeaturesResponseAssert, BatchSolveFeaturesResponse> {

  SolveFeaturesResponseAssert(BatchSolveFeaturesResponse solveFeaturesResponse) {
    super(solveFeaturesResponse, SolveFeaturesResponseAssert.class);
  }

  public static SolveFeaturesResponseAssert assertThat(BatchSolveFeaturesResponse actual) {
    return new SolveFeaturesResponseAssert(actual);
  }

  public SolveFeaturesResponseAssert hasSolutionsCount(int count) {
    if (actual.getSolutionsCount() != count) {
      failWithMessage("Expected solution count to be <%s>, but was <%s>",
          count, actual.getSolutionsCount());
    }

    return this;
  }

  public SolutionResponseAssert solution(int index) {
    return new SolutionResponseAssert(actual.getSolutions(index), this);
  }
}
