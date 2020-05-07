package com.silenteight.sens.webapp.grpc.reasoningbranch;

import lombok.Builder;
import lombok.NonNull;

import com.silenteight.proto.serp.v1.api.BranchChange;
import com.silenteight.proto.serp.v1.api.BranchSolutionChange;
import com.silenteight.proto.serp.v1.api.ChangeBranchesRequest;
import com.silenteight.proto.serp.v1.api.EnablementChange;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;
import com.silenteight.sens.webapp.audit.correlation.RequestCorrelation;
import com.silenteight.sens.webapp.backend.reasoningbranch.BranchId;
import com.silenteight.sens.webapp.backend.reasoningbranch.update.UpdatedBranches;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;

import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static java.util.Optional.ofNullable;

@Builder
public class TestUpdatedBranches implements UpdatedBranches {

  @NonNull
  private final List<BranchId> branchIds;

  @Nullable
  private final String newAiSolution;

  @Nullable
  private final Boolean newStatus;

  @Override
  public Optional<String> getNewAiSolution() {
    return ofNullable(newAiSolution);
  }

  @Override
  public Optional<Boolean> getNewStatus() {
    return ofNullable(newStatus);
  }

  @Override
  public List<BranchId> getBranchIds() {
    return branchIds;
  }

  ChangeBranchesRequest getRequest() {
    BranchChange.Builder branchChange = BranchChange.newBuilder();

    branchChange.setReasoningBranchId(buildGrpcBranchId(getBranchIds().get(0)));

    getNewAiSolution()
        .map(TestUpdatedBranches::buildSolutionChange)
        .ifPresent(branchChange::setSolutionChange);

    getNewStatus()
        .map(TestUpdatedBranches::buildEnablementChange)
        .ifPresent(branchChange::setEnablementChange);

    return ChangeBranchesRequest.newBuilder()
        .addBranchChange(branchChange)
        .setCorrelationId(fromJavaUuid(RequestCorrelation.id()))
        .build();
  }

  private static EnablementChange buildEnablementChange(boolean newStatus) {
    return EnablementChange.newBuilder()
        .setEnabled(newStatus)
        .build();
  }

  private static ReasoningBranchId buildGrpcBranchId(BranchId branchId) {
    return ReasoningBranchId.newBuilder()
        .setFeatureVectorId(branchId.getBranchNo())
        .setDecisionTreeId(branchId.getTreeId())
        .build();
  }

  private static BranchSolutionChange buildSolutionChange(String newSolution) {
    return BranchSolutionChange.newBuilder()
        .setSolution(BranchSolution.valueOf(newSolution))
        .build();
  }
}
