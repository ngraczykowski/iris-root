package com.silenteight.sens.webapp.backend.presentation.dto.decisiontree.status;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class StatusDto {

  @NonNull
  private final String name;
}
