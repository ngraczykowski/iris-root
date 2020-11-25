package com.silenteight.serp.governance.branchquery.dto;

import lombok.Value;

import com.silenteight.proto.serp.v1.governance.ReasoningBranchSummary;

import java.util.List;

@Value
public class ListReasoningBranchDto {
  List<ReasoningBranchSummary> branchSummaries;
  long totalCount;
}
