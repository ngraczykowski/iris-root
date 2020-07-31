package com.silenteight.sens.webapp.backend.reasoningbranch.list.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.backend.parser.ParsedReasoningBranchId;
import com.silenteight.sens.webapp.backend.parser.ReasoningBranchIdParser;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReasoningBranchIdDto {

  private long decisionTreeId;
  private long featureVectorId;

  public static ReasoningBranchIdDto from(String id) {
    ParsedReasoningBranchId branchId = ReasoningBranchIdParser.parse(id);
    return new ReasoningBranchIdDto(
        branchId.getDecisionTreeId(), branchId.getFeatureVectorId());
  }
}
