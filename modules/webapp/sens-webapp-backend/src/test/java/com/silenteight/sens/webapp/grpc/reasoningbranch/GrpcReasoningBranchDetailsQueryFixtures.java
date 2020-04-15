package com.silenteight.sens.webapp.grpc.reasoningbranch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.Value;

import com.silenteight.proto.serp.v1.api.GetReasoningBranchRequest;
import com.silenteight.proto.serp.v1.api.ReasoningBranchResponse;
import com.silenteight.proto.serp.v1.api.ReasoningBranchResponse.Builder;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;

import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.BRANCH_FALSE_POSITIVE;
import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.BRANCH_HINTED_FALSE_POSITIVE;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class GrpcReasoningBranchDetailsQueryFixtures {

  static final ReasoningBranch ENABLED_REASONING_BRANCH =
      new ReasoningBranch(1, 2, true, BRANCH_FALSE_POSITIVE);
  static final ReasoningBranch DISABLED_REASONING_BRANCH =
      new ReasoningBranch(1, 3, false, BRANCH_HINTED_FALSE_POSITIVE);

  @Value
  static class ReasoningBranch {

    long treeId;
    long branchId;
    boolean enabled;
    BranchSolution solution;

    ReasoningBranchResponse asResponse() {
      Builder builder = ReasoningBranchResponse.newBuilder();

      builder.getReasoningBranchBuilder()
          .setEnabled(enabled)
          .setReasoningBranchId(getProtoBranchId())
          .setSolution(solution);

      return builder.build();
    }

    ReasoningBranchId getProtoBranchId() {
      return ReasoningBranchId.newBuilder()
          .setDecisionTreeId(treeId)
          .setFeatureVectorId(branchId)
          .build();
    }

    GetReasoningBranchRequest request() {
      return GetReasoningBranchRequest.newBuilder()
          .setReasoningBranchId(getProtoBranchId())
          .build();
    }
  }
}
