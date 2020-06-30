package com.silenteight.sens.webapp.backend.reasoningbranch.list.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.backend.parser.ParsedReasoningBranchId;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReasoningBranchIdDto {

  private long decisionTreeId;
  private long featureVectorId;

  public static ReasoningBranchIdDto valueOf(ParsedReasoningBranchId reasoningBranchId) {
    return new ReasoningBranchIdDto(
        reasoningBranchId.getDecisionTreeId(), reasoningBranchId.getFeatureVectorId());
  }
}
