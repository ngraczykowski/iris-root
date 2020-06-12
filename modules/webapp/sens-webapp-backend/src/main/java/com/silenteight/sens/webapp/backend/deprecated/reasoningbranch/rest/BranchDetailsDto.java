package com.silenteight.sens.webapp.backend.deprecated.reasoningbranch.rest;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class BranchDetailsDto {

  private final long reasoningBranchId;

  @NonNull
  private final String aiSolution;

  private final boolean isActive;
}
