package com.silenteight.sens.webapp.backend.rest.decisiontree.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class StatusDto {

  @NonNull
  private final String name;
}
