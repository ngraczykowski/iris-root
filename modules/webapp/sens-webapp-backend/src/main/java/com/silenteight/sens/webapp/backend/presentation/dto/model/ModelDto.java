package com.silenteight.sens.webapp.backend.presentation.dto.model;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ModelDto {

  @NonNull
  private final Long id;
  @NonNull
  private final String name;
}
