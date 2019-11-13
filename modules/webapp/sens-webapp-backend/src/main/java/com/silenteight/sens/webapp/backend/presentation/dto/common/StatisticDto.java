package com.silenteight.sens.webapp.backend.presentation.dto.common;

import lombok.Data;
import lombok.NonNull;

@Data
public class StatisticDto {

  @NonNull
  private final String name;
  private final int value;
}
