package com.silenteight.sens.webapp.grpc.reasoningbranch;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.BranchChange;
import com.silenteight.proto.serp.v1.api.BranchSolutionChange;
import com.silenteight.proto.serp.v1.api.ChangeBranchesRequest;
import com.silenteight.proto.serp.v1.api.EnablementChange;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.sens.webapp.backend.reasoningbranch.BranchId;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdatedBranch;

import java.util.Optional;

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Builder
class TestUpdatedBranch implements UpdatedBranch {

  private final long treeId;
  private final long branchId;
  private final String newAiSolution;
  private final Boolean newStatus;

  @Override
  public BranchId getBranchId() {
    return BranchId.of(treeId, branchId);
  }

  @Override
  public Optional<String> getNewAiSolution() {
    return ofNullable(newAiSolution);
  }

  @Override
  public Optional<Boolean> getNewIsActive() {
    return ofNullable(newStatus);
  }

  ChangeBranchesRequest getRequest() {
    BranchChange.Builder branchChange = BranchChange.newBuilder();

    getNewAiSolution()
        .map(TestUpdatedBranch::buildSolutionChange)
        .ifPresent(branchChange::setSolutionChange);

    getNewIsActive()
        .map(TestUpdatedBranch::buildEnablementChange)
        .ifPresent(branchChange::setEnablementChange);

    return ChangeBranchesRequest.newBuilder()
        .addBranchChange(branchChange)
        .build();
  }

  private static EnablementChange buildEnablementChange(boolean newStatus) {
    return EnablementChange.newBuilder()
        .setEnabled(newStatus)
        .build();
  }

  private static BranchSolutionChange buildSolutionChange(String newSolution) {
    return BranchSolutionChange.newBuilder()
        .setSolution(BranchSolution.valueOf(newSolution))
        .build();
  }
}
