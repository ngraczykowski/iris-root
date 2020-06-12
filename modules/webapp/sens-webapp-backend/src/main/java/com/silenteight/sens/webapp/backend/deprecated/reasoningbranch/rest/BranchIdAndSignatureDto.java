package com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
class BranchIdAndSignatureDto {

  private long branchId;

  @NonNull
  private String featureVectorSignature;
}
