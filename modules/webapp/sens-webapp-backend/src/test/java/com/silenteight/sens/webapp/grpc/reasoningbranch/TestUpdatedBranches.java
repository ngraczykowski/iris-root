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
import com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.update.UpdatedBranches;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;

import static com.silenteight.protocol.utils.Uuids.fromJavaUuid;
import static java.util.Optional.ofNullable;

@Builder
public class TestUpdatedBranches implements UpdatedBranches {

  private final long treeId;

  @NonNull
  private final List<Long> branchIds;

  @Nullable
  private final String newAiSolution;

  @Nullable
  private final Boolean newStatus;

  @Nullable
  private final String comment;

  @Override
  public Optional<String> getNewAiSolution() {
    return ofNullable(newAiSolution);
  }

  @Override
  public Optional<Boolean> getNewStatus() {
    return ofNullable(newStatus);
  }

  @Override
  public Optional<String> getComment() {
    return ofNullable(comment);
  }

  @Override
  public long getTreeId() {
    return treeId;
  }

  @Override
  public List<Long> getBranchIds() {
    return branchIds;
  }

  ChangeBranchesRequest getRequest() {
    BranchChange.Builder branchChange = BranchChange.newBuilder();

    branchChange.setReasoningBranchId(buildGrpcBranchId(getTreeId(), getBranchIds().get(0)));

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

  private static ReasoningBranchId buildGrpcBranchId(Long treeId, Long branchId) {
    return ReasoningBranchId.newBuilder()
        .setDecisionTreeId(treeId)
        .setFeatureVectorId(branchId)
        .build();
  }

  private static BranchSolutionChange buildSolutionChange(String newSolution) {
    return BranchSolutionChange.newBuilder()
        .setSolution(BranchSolution.valueOf(newSolution))
        .build();
  }
}
