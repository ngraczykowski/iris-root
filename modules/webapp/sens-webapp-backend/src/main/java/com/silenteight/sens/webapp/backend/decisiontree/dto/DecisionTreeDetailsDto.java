package com.silenteight.sens.webapp.backend.decisiontree.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class DecisionTreeDetailsDto {

  private final long id;
  @NonNull
  private final String name;
  @NonNull
  private final StatusDto status;
}
