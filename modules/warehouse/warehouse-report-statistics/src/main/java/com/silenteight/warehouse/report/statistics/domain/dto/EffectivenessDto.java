package com.silenteight.warehouse.report.statistics.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EffectivenessDto {

  private long solvedAlerts;
  private long allAlerts;
}
