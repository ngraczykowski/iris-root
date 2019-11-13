package com.silenteight.sens.webapp.backend.presentation.dto.decisiontree.details;

import lombok.Data;
import lombok.NonNull;

@Data
public class OutputPortDto {

  @NonNull
  private final String name;
  @NonNull
  private final String url;
}
