package com.silenteight.sens.webapp.backend.rest.decisiontree.dto;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class DecisionTreesDto {

  private final int total;
  @NonNull
  private final List<DecisionTreeDto> results;
}
