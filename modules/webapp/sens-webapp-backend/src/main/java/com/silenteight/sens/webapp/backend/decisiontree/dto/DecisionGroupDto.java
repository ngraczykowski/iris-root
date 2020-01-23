package com.silenteight.sens.webapp.backend.decisiontree.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class DecisionGroupDto {

  @NonNull
  private final String name;
}
