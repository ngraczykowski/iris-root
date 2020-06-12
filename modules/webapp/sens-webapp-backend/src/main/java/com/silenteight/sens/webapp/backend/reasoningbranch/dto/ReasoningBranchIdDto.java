package com.silenteight.sens.webapp.backend.reasoningbranch.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReasoningBranchIdDto {

  private final long decisionTreeId;
  private final long featureVectorId;
}
