package com.silenteight.sens.webapp.backend.circuitbreaker;

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

  static ReasoningBranchIdDto valueOf(ParsedReasoningBranchId reasoningBranchId) {
    return new ReasoningBranchIdDto(
        reasoningBranchId.getDecisionTreeId(), reasoningBranchId.getFeatureVectorId());
  }
}
