package com.silenteight.sens.webapp.backend.presentation.dto.decisiontree;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class AgentDto {

  @NonNull
  private final String name;
  @NonNull
  private final String description;
}
