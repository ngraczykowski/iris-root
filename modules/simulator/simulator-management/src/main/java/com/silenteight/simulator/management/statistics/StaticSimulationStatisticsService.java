package com.silenteight.simulator.management.statistics;

import com.silenteight.simulator.management.statistics.dto.EffectivenessDto;
import com.silenteight.simulator.management.statistics.dto.EfficiencyDto;

import java.util.UUID;

class StaticSimulationStatisticsService {

  static final long AI_SOLVED_AS_FALSE_POSITIVE = 3632L;
  static final long ANALYSTS_SOLVED_AS_FALSE_POSITIVE = 3632L;
  static final long SOLVED_ALERTS = 3632L;
  static final long ALL_ALERTS = 10000L;

  public EfficiencyDto getEfficiency(UUID simulationId) {
    return EfficiencyDto
        .builder()
        .solvedAlerts(SOLVED_ALERTS)
        .allAlerts(ALL_ALERTS)
        .build();
  }

  public EffectivenessDto getEffectiveness(UUID simulationId) {
    return EffectivenessDto
        .builder()
        .aiSolvedAsFalsePositive(AI_SOLVED_AS_FALSE_POSITIVE)
        .analystSolvedAsFalsePositive(ANALYSTS_SOLVED_AS_FALSE_POSITIVE)
        .build();
  }
}
