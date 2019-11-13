package com.silenteight.sens.webapp.backend.presentation.dto.decisiontree;

import lombok.Data;
import lombok.NonNull;

import java.util.List;

@Data
public class DecisionTreeResponseDto {

  private final int total;
  @NonNull
  private final List<DecisionTreeDto> results;
}
