package com.silenteight.sens.webapp.backend.bulkchange;

import lombok.Data;
import lombok.NonNull;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;

@Data
public class BulkChangeDto {

  @NonNull
  private final UUID id;
  @NonNull
  private final List<ReasoningBranchIdDto> reasoningBranchIds;
  @Nullable
  private final String aiSolution;
  @Nullable
  private final Boolean active;
  @NonNull
  private final OffsetDateTime createdAt;
}
