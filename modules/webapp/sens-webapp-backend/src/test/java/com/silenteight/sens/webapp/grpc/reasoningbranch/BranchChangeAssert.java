package com.silenteight.sens.webapp.grpc.reasoningbranch;

import com.silenteight.proto.serp.v1.api.BranchChange;
import com.silenteight.proto.serp.v1.api.BranchSolutionChange;
import com.silenteight.proto.serp.v1.api.EnablementChange;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.sens.webapp.backend.reasoningbranch.BranchId;

import org.assertj.core.api.AbstractAssert;

import static org.assertj.core.api.Assertions.*;

public class BranchChangeAssert extends AbstractAssert<BranchChangeAssert, BranchChange> {

  private BranchChangeAssert(BranchChange branchChange) {
    super(branchChange, BranchChangeAssert.class);
  }

  static BranchChangeAssert assertThatBranchChange(BranchChange branchChange) {
    return new BranchChangeAssert(branchChange);
  }

  BranchChangeAssert hasEnablementChange(boolean value) {
    assertThat(actual.getEnablementChange())
        .extracting(EnablementChange::getEnabled)
        .isEqualTo(value);

    return this;
  }

  BranchChangeAssert hasAiSolutionChange(String solutionText) {
    BranchSolution solution = BranchSolution.valueOf(solutionText);

    return hasAiSolutionChange(solution);
  }

  BranchChangeAssert hasAiSolutionChange(BranchSolution branchSolution) {
    assertThat(actual.getSolutionChange())
        .extracting(BranchSolutionChange::getSolution)
        .isEqualTo(branchSolution);

    return this;
  }

  BranchChangeAssert hasBranchId(BranchId branchId) {
    assertThat(actual.getReasoningBranchId())
        .extracting(ReasoningBranchId::getDecisionTreeId)
        .isEqualTo(branchId.getTreeId());

    assertThat(actual.getReasoningBranchId())
        .extracting(ReasoningBranchId::getFeatureVectorId)
        .isEqualTo(branchId.getBranchNo());

    return this;
  }
}
