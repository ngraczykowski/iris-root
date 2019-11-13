package com.silenteight.sens.webapp.backend.presentation.dto.decisiontree;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class FeatureDto {

  @NonNull
  private final String name;

  private final String description;
}
