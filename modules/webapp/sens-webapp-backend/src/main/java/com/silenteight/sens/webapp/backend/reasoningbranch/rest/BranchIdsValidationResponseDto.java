package com.silenteight.sens.webapp.backend.reasoningbranch.rest;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BranchIdsValidationResponseDto {

  @NonNull
  private List<BranchIdAndSignatureDto> branchIds;
}
