package com.silenteight.sens.webapp.backend.bulkchange;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.silenteight.sens.webapp.backend.reasoningbranch.dto.ReasoningBranchIdDto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BulkChangeDto {

  @NotNull
  private UUID id;
  @NotEmpty
  private List<ReasoningBranchIdDto> reasoningBranchIds;
  @Nullable
  private String aiSolution;
  @Nullable
  private Boolean active;
  @NotNull
  private OffsetDateTime createdAt;
}
