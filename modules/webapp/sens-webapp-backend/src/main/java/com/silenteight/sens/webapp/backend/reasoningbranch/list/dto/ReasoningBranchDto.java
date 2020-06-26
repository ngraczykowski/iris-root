package com.silenteight.sens.webapp.backend.reasoningbranch.list.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.Instant;
import javax.annotation.Nullable;

@Data
@Builder
public class ReasoningBranchDto {

  @NonNull
  private final ReasoningBranchIdDto reasoningBranchId;
  @NonNull
  private final String aiSolution;
  private final boolean active;
  @NonNull
  private final Instant createdAt;
  @Nullable
  private final Instant updatedAt;
}
