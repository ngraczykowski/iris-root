package com.silenteight.sens.webapp.backend.circuitbreaker;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DiscrepantBranchDto {

  @NonNull
  private ReasoningBranchIdDto branchId;
  @NonNull
  private Instant detectedAt;
}
