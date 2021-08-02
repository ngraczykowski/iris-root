package com.silenteight.warehouse.report.statistics.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

@Data
@Builder
public class StatisticsDto {

  @NonNull
  private EfficiencyDto efficiency;
  @NonNull
  private EffectivenessDto effectiveness;
}
