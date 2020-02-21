package com.silenteight.sens.webapp.grpc.reasoningbranch;

import com.silenteight.proto.serp.v1.api.ChangeBranchesRequest;
import com.silenteight.sens.webapp.backend.reasoningbranch.BranchId;

import org.assertj.core.api.AbstractAssert;

import static com.silenteight.sens.webapp.grpc.reasoningbranch.BranchChangeAssert.assertThatBranchChange;
import static org.assertj.core.api.Assertions.*;

public class ChangeBranchesRequestAssert
    extends AbstractAssert<ChangeBranchesRequestAssert, ChangeBranchesRequest> {

  private ChangeBranchesRequestAssert(
      ChangeBranchesRequest changeBranchesRequest) {
    super(changeBranchesRequest, ChangeBranchesRequestAssert.class);
  }

  static ChangeBranchesRequestAssert assertThatChangeBranchRequest(ChangeBranchesRequest asserted) {
    return new ChangeBranchesRequestAssert(asserted);
  }

  ChangeBranchesRequestAssert hasStatusChange(boolean status) {
    assertThat(actual.getBranchChangeList())
        .anySatisfy(branchChange -> assertThatBranchChange(branchChange)
            .hasEnablementChange(status));

    return this;
  }

  ChangeBranchesRequestAssert hasSolutionChange(String solution) {
    assertThat(actual.getBranchChangeList())
        .anySatisfy(branchChange -> assertThatBranchChange(branchChange)
            .hasAiSolutionChange(solution));

    return this;
  }

  ChangeBranchesRequestAssert hasBranchId(BranchId branchId) {
    assertThat(actual.getBranchChangeList())
        .anySatisfy(branchChange -> assertThatBranchChange(branchChange)
            .hasBranchId(branchId));

    return this;
  }

  ChangeBranchesRequestAssert sameBranchChangeHasSolutionAndStatusChanges(
      String solution, boolean status) {
    assertThat(actual.getBranchChangeList())
        .anySatisfy(branchChange -> assertThatBranchChange(branchChange)
            .hasAiSolutionChange(solution)
            .hasEnablementChange(status));

    return this;
  }
}
