package com.silenteight.sens.webapp.backend.presentation.dto.common;

import lombok.Data;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

@Data
public class StatisticGroupDto {

  @NonNull
  private final String name;
  private final List<StatisticDto> stats = new ArrayList<>();
}
