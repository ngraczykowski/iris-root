package com.silenteight.warehouse.report.statistics.simulation.query;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.report.reporting.StatisticsProperties;
import com.silenteight.warehouse.report.statistics.simulation.dto.SimulationStatisticsDto;
import com.silenteight.warehouse.report.statistics.simulation.dto.SimulationStatisticsDto.EffectivenessDto;
import com.silenteight.warehouse.report.statistics.simulation.dto.SimulationStatisticsDto.EfficiencyDto;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
class SimulationStatisticsQuery implements StatisticsQuery {

  private static final String ANALYSIS_PREFIX = "analysis/";

  @NonNull
  private final JdbcTemplate jdbcTemplate;
  @Valid
  @NonNull
  private final StatisticsProperties properties;

  @Override
  public SimulationStatisticsDto getStatistics(String analysisId) {
    String analysisParameter = getAnalysisParameter(analysisId);
    EfficiencyDto efficiency = toEfficiencyDto(analysisParameter);
    EffectivenessDto effectiveness = toEffectivenessDto(analysisParameter);

    log.debug(
        "Analysis={}, efficiency={}, effectiveness={}", analysisId, efficiency, effectiveness);
    return SimulationStatisticsDto
        .builder()
        .efficiency(efficiency)
        .effectiveness(effectiveness)
        .build();
  }

  private EfficiencyDto toEfficiencyDto(String analysisId) {
    long solvedAlertsCount = countSolvedAlerts(analysisId);
    long alertsCount = countAllAlerts(analysisId);

    return EfficiencyDto
        .builder()
        .allAlerts(alertsCount)
        .solvedAlerts(solvedAlertsCount)
        .build();
  }

  private EffectivenessDto toEffectivenessDto(String analysisParameter) {
    long analystFalsePositiveCount = countSolvedAsFalsePositiveByAnalyst(analysisParameter);
    long aiFalsePositiveCount = countSolvedAsFalsePositiveByAi(analysisParameter);

    return EffectivenessDto
        .builder()
        .analystSolvedAsFalsePositive(analystFalsePositiveCount)
        .aiSolvedAsFalsePositive(aiFalsePositiveCount)
        .build();
  }

  private long countSolvedAlerts(String analysisParameter) {
    String solvedAlertsQuery = properties.getSolvedAlertsQuery();
    return getCount(solvedAlertsQuery, analysisParameter);
  }

  private long countAllAlerts(String analysisParameter) {
    String allAlertsQuery = properties.getAllAlertsQuery();
    return getCount(allAlertsQuery, analysisParameter);
  }

  private long countSolvedAsFalsePositiveByAnalyst(String analysisParameter) {
    String analystFalsePositiveQuery = properties.getAnalystFalsePositiveQuery();
    return getCount(analystFalsePositiveQuery, analysisParameter);
  }

  private long countSolvedAsFalsePositiveByAi(String analysisParameter) {
    String aiFalsePositiveQuery = properties.getAiFalsePositiveQuery();
    return getCount(aiFalsePositiveQuery, analysisParameter);
  }

  private Long getCount(String queryTemplate, String analysisParameter) {
    return jdbcTemplate.queryForObject(queryTemplate, Long.class, analysisParameter);
  }

  private static String getAnalysisParameter(String analysisId) {
    return ANALYSIS_PREFIX + analysisId;
  }
}
