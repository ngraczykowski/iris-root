package com.silenteight.sens.webapp.backend.bulkchange;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

@Value
@Builder
public class CreateBulkChangeCommand {

  @NonNull
  private UUID bulkChangeId;
  @NonNull
  private List<ReasoningBranchIdDto> reasoningBranchIds;
  @Nullable
  private String aiSolution;
  @Nullable
  private Boolean active;
  @NotNull
  private OffsetDateTime cratedAt;
}
