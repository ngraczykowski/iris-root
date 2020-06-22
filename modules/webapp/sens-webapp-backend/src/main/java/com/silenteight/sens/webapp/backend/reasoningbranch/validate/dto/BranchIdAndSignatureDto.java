package com.silenteight.sens.webapp.backend.reasoningbranch.validate.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchIdAndSignatureDto {

  private long branchId;
  @NonNull
  private String featureVectorSignature;
}
