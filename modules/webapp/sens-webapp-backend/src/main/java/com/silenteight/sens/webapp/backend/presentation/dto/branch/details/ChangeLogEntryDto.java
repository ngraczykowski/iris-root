package com.silenteight.sens.webapp.backend.presentation.dto.branch.details;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;

@Data
@Builder
public class ChangeLogEntryDto {

  private String changeType;
  private String description;
  private String username;
  private Instant date;
}
