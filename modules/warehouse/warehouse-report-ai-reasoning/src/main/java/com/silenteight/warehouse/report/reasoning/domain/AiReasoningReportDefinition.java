package com.silenteight.warehouse.report.reasoning.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reasoning.domain.exception.ReportTypeNotFoundException;
import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.Period;
import java.util.List;

import static com.silenteight.sep.base.common.time.DefaultTimeSource.TIME_CONVERTER;
import static java.time.Period.ofDays;
import static java.time.Period.ofYears;
import static java.util.Arrays.stream;
import static java.util.function.Predicate.not;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Getter
public enum AiReasoningReportDefinition {

  MONTH(
      "1d108cb4-a852-4851-9f20-e3818243dc29",
      "ai-reasoning-30-days.csv",
      "AI Reasoning - Last 30 days",
      "AI Reasoning report - Last 30 days",
      ofDays(30),
      true),
  SIMULATION(
      "222c75c8-7261-4444-a4f3-722dc3235201",
      "ai-reasoning.csv",
      "AI Reasoning",
      "Simulation AI Reasoning report",
      ofYears(Constants.SIMULATION_RANGE_YEARS),
      false);

  private static final String PRODUCTION_ANALYSIS_NAME = "production";
  private static final String REPORT_TYPE = "AI_REASONING";
  @NonNull
  private final String id;
  @NonNull
  private final String filename;
  @NonNull
  private final String title;
  @NonNull
  private final String description;
  @NonNull
  private final Period reportRange;
  private final boolean production;

  public static List<ReportDefinitionDto> toProductionReportsDefinitionDto() {
    return stream(AiReasoningReportDefinition.values())
        .filter(AiReasoningReportDefinition::isProduction)
        .map(reportDefinition -> reportDefinition.toReportDefinitionDto(PRODUCTION_ANALYSIS_NAME))
        .collect(toList());
  }

  public static List<ReportDefinitionDto> toSimulationReportsDefinitionDto(String analysisId) {
    return stream(AiReasoningReportDefinition.values())
        .filter(not(AiReasoningReportDefinition::isProduction))
        .map(reportDefinition -> reportDefinition.toReportDefinitionDto(analysisId))
        .collect(toList());
  }

  public static AiReasoningReportDefinition getReportType(String id) {
    return stream(values())
        .filter(reportDefinition -> reportDefinition.hasId(id))
        .findAny()
        .orElseThrow(() -> new ReportTypeNotFoundException(id));
  }

  private ReportDefinitionDto toReportDefinitionDto(String analysisId) {
    return ReportDefinitionDto
        .builder()
        .id(id)
        .name(getReportName(analysisId, id))
        .title(title)
        .description(description)
        .reportType(REPORT_TYPE)
        .build();
  }

  OffsetDateTime getFrom(Instant now) {
    return TIME_CONVERTER.toOffset(now).minus(reportRange);
  }

  OffsetDateTime getTo(Instant now) {
    return TIME_CONVERTER.toOffset(now);
  }

  private boolean hasId(String id) {
    return this.getId().equals(id);
  }

  private String getReportName(String analysisId, String id) {
    return "analysis/" + analysisId + "/definitions/" + REPORT_TYPE + "/" + id + "/reports";
  }

  private static class Constants {

    // For the simulation we need to take all alerts, we will have only 6 months data retention so
    // 10 years looks like a safe margin.
    private static final int SIMULATION_RANGE_YEARS = 10;
  }
}
