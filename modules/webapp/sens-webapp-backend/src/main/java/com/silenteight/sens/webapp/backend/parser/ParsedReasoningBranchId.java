package com.silenteight.sens.webapp.backend.parser;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class ParsedReasoningBranchId {

  private final long decisionTreeId;
  private final long featureVectorId;
}
