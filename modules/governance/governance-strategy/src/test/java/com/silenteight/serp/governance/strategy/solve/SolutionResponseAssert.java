package com.silenteight.serp.governance.strategy.solve;

import com.silenteight.solving.api.v1.SolveAlertSolutionResponse;

import org.assertj.core.api.AbstractAssert;

import javax.annotation.Nullable;

class SolutionResponseAssert extends
    AbstractAssert<SolutionResponseAssert, SolveAlertSolutionResponse> {

  @Nullable
  private final SolveAlertsResponseAssert parent;

  SolutionResponseAssert(
      SolveAlertSolutionResponse solutionResponse, SolveAlertsResponseAssert parent) {

    super(solutionResponse, SolutionResponseAssert.class);
    this.parent = parent;
  }

  SolutionResponseAssert hasAlertSolution(String solution) {
    if (!actual.getAlertSolution().equals(solution)) {
      failWithMessage("Expected alert solution to be <%s>, but was <%s>",
          solution, actual.getAlertSolution());
    }

    return this;
  }

  public SolutionResponseAssert hasAlertName(String name) {
    if (!actual.getAlertName().equals(name)) {
      failWithMessage("Expected alert name to be <%s>, but was <%s>",
          name, actual.getAlertName());
    }

    return this;
  }

  public SolveAlertsResponseAssert and() {
    return parent;
  }
}
