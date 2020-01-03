package com.silenteight.sens.webapp.backend.rest.model.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class ModelDto {

  private final long id;
  @NonNull
  private final String name;
}
