package com.silenteight.sens.webapp.grpc.reasoningbranch;

import lombok.Value;

import com.silenteight.proto.serp.v1.api.GetReasoningBranchRequest;
import com.silenteight.proto.serp.v1.api.ReasoningBranchResponse;
import com.silenteight.proto.serp.v1.api.ReasoningBranchResponse.Builder;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.recommendation.BranchSolution;

import com.google.rpc.Code;
import com.google.rpc.Status;
import io.grpc.StatusRuntimeException;

import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.*;
import static io.grpc.protobuf.StatusProto.toStatusRuntimeException;

class GrpcReasoningBranchDetailsQueryFixtures {

  static final ReasoningBranch ENABLED_REASONING_BRANCH =
      new ReasoningBranch(1, 2, true, BRANCH_FALSE_POSITIVE);
  static final ReasoningBranch DISABLED_REASONING_BRANCH =
      new ReasoningBranch(1, 3, false, BRANCH_HINTED_FALSE_POSITIVE);
  static final Status NOT_FOUND_STATUS =
      Status.newBuilder().setCode(Code.NOT_FOUND_VALUE).build();
  static final StatusRuntimeException NOT_FOUND_RUNTIME_EXCEPTION = toStatusRuntimeException(
      NOT_FOUND_STATUS);
  static final StatusRuntimeException OTHER_STATUS_RUNTIME_EXCEPTION = toStatusRuntimeException(
      Status.newBuilder().setCode(Code.INTERNAL_VALUE).build()
  );

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
