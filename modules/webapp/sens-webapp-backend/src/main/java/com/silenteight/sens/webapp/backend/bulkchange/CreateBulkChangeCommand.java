package com.silenteight.sens.webapp.backend.bulkchange;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.sens.webapp.backend.reasoningbranch.dto.ReasoningBranchIdDto;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

import static java.util.Optional.ofNullable;

@Value
@Builder
public class CreateBulkChangeCommand {

  @NonNull
  UUID bulkChangeId;
  @NonNull
  List<ReasoningBranchIdDto> reasoningBranchIds;
  @Nullable
  String aiSolution;
  @Nullable
  Boolean active;
  @NotNull
  OffsetDateTime cratedAt;

  Optional<Boolean> getEnablement() {
    return ofNullable(active);
  }

  Optional<String> getSolution() {
    return ofNullable(aiSolution);
  }
}
