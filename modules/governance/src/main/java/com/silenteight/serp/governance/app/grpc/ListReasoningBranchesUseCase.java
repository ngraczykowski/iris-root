package com.silenteight.serp.governance.app.grpc;

import lombok.RequiredArgsConstructor;

import com.silenteight.proto.serp.v1.api.ListReasoningBranchesRequest;
import com.silenteight.proto.serp.v1.api.ListReasoningBranchesResponse;
import com.silenteight.proto.serp.v1.common.Page;
import com.silenteight.serp.governance.branchquery.ReasoningBranchFinder;
import com.silenteight.serp.governance.branchquery.dto.ListReasoningBranchDto;

import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
class ListReasoningBranchesUseCase {

  private final ReasoningBranchFinder branchFinder;

  ListReasoningBranchesResponse activate(ListReasoningBranchesRequest request) {
    return mapToListResponse(branchFinder.findAll(request));
  }

  @NotNull
  private static ListReasoningBranchesResponse mapToListResponse(
      ListReasoningBranchDto listReasoningBranchDto) {
    return ListReasoningBranchesResponse
        .newBuilder()
        .addAllReasoningBranch(listReasoningBranchDto.getBranchSummaries())
        .setPage(Page.newBuilder().setTotalElements(listReasoningBranchDto.getTotalCount()).build())
        .build();
  }
}
