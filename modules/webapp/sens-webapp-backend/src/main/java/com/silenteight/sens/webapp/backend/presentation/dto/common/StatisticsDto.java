package com.silenteight.sens.webapp.backend.presentation.dto.common;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.util.Map;

@Builder
@Data
public class StatisticsDto {

  private final long total;
  @NonNull
  private final Map<String, Long> stats;
}
