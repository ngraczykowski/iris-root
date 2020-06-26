package com.silenteight.sens.webapp.grpc.reasoningbranch;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.proto.serp.v1.api.ListReasoningBranchesRequest;
import com.silenteight.proto.serp.v1.api.ListReasoningBranchesRequest.DecisionTreeFilter;
import com.silenteight.proto.serp.v1.api.ListReasoningBranchesResponse;
import com.silenteight.proto.serp.v1.common.Pagination;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchSummary;

import java.util.List;

import static com.silenteight.proto.serp.v1.recommendation.BranchSolution.BRANCH_FALSE_POSITIVE;
import static java.util.Arrays.asList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class GrpcReasoningBranchesQueryTestFixtures {

  static final long DECISION_TREE_ID = 1;
  static final long EXISTING_REASONING_BRANCH_ID = 1;
  static final long NOT_EXISTING_REASONING_BRANCH_ID = 2;
  static final List<Long> REASONING_BRANCH_IDS =
      asList(EXISTING_REASONING_BRANCH_ID, NOT_EXISTING_REASONING_BRANCH_ID);

  private static final DecisionTreeFilter DECISION_TREE_FILTER = DecisionTreeFilter
      .newBuilder()
      .addDecisionTreeIds(DECISION_TREE_ID)
      .build();

  static final ListReasoningBranchesRequest LIST_REASONING_BRANCHES_REQUEST =
      ListReasoningBranchesRequest
          .newBuilder()
          .setDecisionTreeFilter(DECISION_TREE_FILTER)
          .setPagination(Pagination.newBuilder().setPageSize(Integer.MAX_VALUE).build())
          .build();

  private static final ReasoningBranchId SEARCHED_REASONING_BRANCH_ID =
      ReasoningBranchId
          .newBuilder()
          .setDecisionTreeId(DECISION_TREE_ID)
          .setFeatureVectorId(1)
          .build();

  private static final ReasoningBranchSummary SEARCHED_REASONING_BRANCH =
      ReasoningBranchSummary
          .newBuilder()
          .setReasoningBranchId(SEARCHED_REASONING_BRANCH_ID)
          .setSolution(BRANCH_FALSE_POSITIVE)
          .setEnabled(true)
          .build();

  private static final ReasoningBranchId NOT_SEARCHED_REASONING_BRANCH_ID =
      ReasoningBranchId
          .newBuilder()
          .setDecisionTreeId(DECISION_TREE_ID)
          .setFeatureVectorId(3)
          .build();

  private static final ReasoningBranchSummary NOT_SEARCHED_REASONING_BRANCH =
      ReasoningBranchSummary
          .newBuilder()
          .setReasoningBranchId(NOT_SEARCHED_REASONING_BRANCH_ID)
          .setSolution(BRANCH_FALSE_POSITIVE)
          .setEnabled(true)
          .build();

  static final ListReasoningBranchesResponse LIST_REASONING_BRANCHES_RESPONSE =
      ListReasoningBranchesResponse
          .newBuilder()
          .addAllReasoningBranch(asList(SEARCHED_REASONING_BRANCH, NOT_SEARCHED_REASONING_BRANCH))
          .build();
}
