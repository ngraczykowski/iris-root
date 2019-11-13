package com.silenteight.sens.webapp.backend.presentation.dto.branch.details;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
@Builder
public class ChangeLogDto {

  @NonNull
  private final List<ChangeLogEntryDto> entries;
}
