package com.silenteight.sens.webapp.backend.presentation.dto.common;

import lombok.Data;
import lombok.NonNull;

@Data
class FieldDto {

  @NonNull
  private final String name;
  private final Object value;
}
