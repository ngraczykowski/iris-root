package com.silenteight.serp.governance.policy.solve;

import com.silenteight.proto.governance.v1.api.GetSolutionsResponse;

import org.assertj.core.api.AbstractAssert;

public class GetSolutionsResponseAssert extends
    AbstractAssert<GetSolutionsResponseAssert, GetSolutionsResponse> {

  GetSolutionsResponseAssert(GetSolutionsResponse getSolutionsResponse) {
    super(getSolutionsResponse, GetSolutionsResponseAssert.class);
  }

  public static GetSolutionsResponseAssert assertThat(GetSolutionsResponse actual) {
    return new GetSolutionsResponseAssert(actual);
  }

  public GetSolutionsResponseAssert hasSolutionsCount(int count) {
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
