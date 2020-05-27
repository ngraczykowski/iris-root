package com.silenteight.sens.webapp.backend.bulkchange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReasoningBranchIdDto {
  
  private long decisionTreeId;
  private long featureVectorId;
}
