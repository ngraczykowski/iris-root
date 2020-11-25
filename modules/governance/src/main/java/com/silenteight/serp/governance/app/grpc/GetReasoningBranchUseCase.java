package com.silenteight.serp.governance.app.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.GetReasoningBranchRequest;
import com.silenteight.proto.serp.v1.api.ReasoningBranchResponse;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchSummary;
import com.silenteight.serp.governance.branchquery.ReasoningBranchFinder;

import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
class GetReasoningBranchUseCase {

  private final ReasoningBranchFinder branchFinder;

  ReasoningBranchResponse activate(GetReasoningBranchRequest request) {
    ReasoningBranchId reasoningBranchId = request.getReasoningBranchId();
    long decisionTreeId = reasoningBranchId.getDecisionTreeId();
    long featureVectorId = reasoningBranchId.getFeatureVectorId();

    ReasoningBranchSummary branch = branchFinder
        .getByDecisionTreeIdAndFeatureVectorId(decisionTreeId, featureVectorId);

    return mapToSingleBranchResponse(branch);
  }


  @NotNull
  private static ReasoningBranchResponse mapToSingleBranchResponse(ReasoningBranchSummary branch) {
    return ReasoningBranchResponse.newBuilder().setReasoningBranch(branch).build();
  }
}
