package com.silenteight.sens.webapp.backend.presentation.dto.branch;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class FeatureDto {

  @NonNull
  private final String name;
  private final String value;
}
