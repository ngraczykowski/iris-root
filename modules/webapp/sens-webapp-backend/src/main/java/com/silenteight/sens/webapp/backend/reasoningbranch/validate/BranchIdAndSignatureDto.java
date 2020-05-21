package com.silenteight.sens.webapp.backend.reasoningbranch.validate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchIdAndSignatureDto {

  private long reasoningBranchId;
  @NonNull
  private String featureVectorSignature;
}
