package com.silenteight.serp.governance.branchquery.dto;

import lombok.Value;

import com.silenteight.proto.serp.v1.governance.ReasoningBranchId;
import com.silenteight.proto.serp.v1.governance.ReasoningBranchSummary;

import org.jetbrains.annotations.NotNull;

import java.util.List;

@Value
public class ReasoningBranchSummaryDto {

  long decisionTreeId;
  long featureVectorId;
  @NotNull
  List<String> featureValues;
  @NotNull
  String solution;
  boolean enabled;

  public ReasoningBranchSummaryDto(ReasoningBranchSummary summary) {
    ReasoningBranchId reasoningBranchId = summary.getReasoningBranchId();
    this.decisionTreeId = reasoningBranchId.getDecisionTreeId();
    this.featureVectorId = reasoningBranchId.getFeatureVectorId();
    this.featureValues = summary.getFeatureValueList();
    this.solution = summary.getSolution().name();
    this.enabled = summary.getEnabled();
  }
}
