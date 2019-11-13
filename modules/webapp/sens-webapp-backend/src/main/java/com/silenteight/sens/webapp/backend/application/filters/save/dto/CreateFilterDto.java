package com.silenteight.sens.webapp.backend.application.filters.save.dto;

import lombok.Data;
import lombok.NonNull;

@Data
public class CreateFilterDto {

  @NonNull
  private String name;
  @NonNull
  private String query;
}
