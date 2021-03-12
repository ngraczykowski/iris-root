package com.silenteight.serp.governance.strategy.solve;

import com.silenteight.solving.api.v1.BatchSolveAlertsResponse;

import org.assertj.core.api.AbstractAssert;

class SolveAlertsResponseAssert extends
    AbstractAssert<SolveAlertsResponseAssert, BatchSolveAlertsResponse> {

  SolveAlertsResponseAssert(BatchSolveAlertsResponse response) {
    super(response, SolveAlertsResponseAssert.class);
  }

  public static SolveAlertsResponseAssert assertThat(BatchSolveAlertsResponse actual) {
    return new SolveAlertsResponseAssert(actual);
  }

  public SolveAlertsResponseAssert hasSolutionsCount(int count) {
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
