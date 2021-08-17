package com.silenteight.warehouse.report.statistics;

import com.silenteight.warehouse.report.statistics.domain.dto.EffectivenessDto;
import com.silenteight.warehouse.report.statistics.domain.dto.EfficiencyDto;
import com.silenteight.warehouse.report.statistics.domain.dto.StatisticsDto;

public final class StatisticsTestFixtures {

  public static final EffectivenessDto EFFECTIVENESS_DTO = EffectivenessDto
      .builder()
      .analystSolvedAsFalsePositive(10L)
      .aiSolvedAsFalsePositive(5L)
      .build();

  public static final EfficiencyDto EFFICIENCY_DTO = EfficiencyDto
      .builder()
      .solvedAlerts(20L)
      .allAlerts(25L)
      .build();

  public static final StatisticsDto STATISTICS_DTO = StatisticsDto
      .builder()
      .effectiveness(EFFECTIVENESS_DTO)
      .efficiency(EFFICIENCY_DTO)
      .build();
}
