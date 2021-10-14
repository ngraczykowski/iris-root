package com.silenteight.warehouse.report.metrics.v1.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.warehouse.report.metrics.v1.domain.exception.ReportTypeNotFoundException;
import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.Period;
import java.util.List;

import static java.time.Period.ofDays;
import static java.time.Period.ofYears;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Getter
public enum DeprecatedMetricsReportDefinition {

  DAY(
      "da752407-6047-423a-b7d6-45a72462cec8",
      "metrics-1-day.csv",
      "Metrics - Last day",
      "Metrics report - Last day",
      ofDays(1),
      true),
  WEEK(
      "48d74712-9d0f-4a97-a4be-3d1e74fa58e1",
      "metrics-7-days.csv",
      "Metrics - Last 7 days",
      "Metrics report - Last 7 days",
      ofDays(7),
      true),
  MONTH(
      "6a08aebb-dc9a-48e8-b3bb-48413d93fdd3",
      "metrics-30-days.csv",
      "Metrics - Last 30 days",
      "Metrics report - Last 30 days",
      ofDays(30),
      true),
  THREE_MONTHS(
      "cc71c156-c4f0-4626-a901-90f54f28dff8",
      "metrics-90-days.csv",
      "Metrics - Last 90 days",
      "Metrics report - Last 90 days",
      ofDays(90),
      true),
  SIMULATION(
      "1acb8a9f-c560-4b5c-95a3-c69bcf32b22e",
      "simulation-metrics.csv",
      "Simulation - metrics",
      "Simulation metrics report",
      ofYears(Constants.SIMULATION_RANGE_YEARS),
      false);

  private static final String REPORT_TYPE = "METRICS";
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
  private static final String PRODUCTION_ANALYSIS_NAME = "production";

  public static List<ReportDefinitionDto> toProductionReportsDefinitionDto() {
    return stream(DeprecatedMetricsReportDefinition.values())
        .filter(DeprecatedMetricsReportDefinition::isProduction)
        .map(reportDefinition -> reportDefinition.toReportDefinitionDto(PRODUCTION_ANALYSIS_NAME))
        .collect(toList());
  }

  public static DeprecatedMetricsReportDefinition getReportType(String id) {
    return stream(values())
        .filter(reportDefinition -> reportDefinition.hasId(id))
        .findAny()
        .orElseThrow(() -> new ReportTypeNotFoundException(id));
  }

  public static List<ReportDefinitionDto> toReportsDefinitionDto(String analysisId) {
    return stream(DeprecatedMetricsReportDefinition.values())
        .filter(reportDefinition1 -> !reportDefinition1.isProduction())
        .map(reportDefinition -> reportDefinition.toReportDefinitionDto(analysisId))
        .collect(toList());
  }

  OffsetDateTime getFrom(Instant now) {
    return DefaultTimeSource.TIME_CONVERTER.toOffset(now).minus(reportRange);
  }

  OffsetDateTime getTo(Instant now) {
    return DefaultTimeSource.TIME_CONVERTER.toOffset(now);
  }

  private boolean hasId(String id) {
    return getId().equals(id);
  }

  private ReportDefinitionDto toReportDefinitionDto(String analysisId) {
    return ReportDefinitionDto
        .builder()
        .id(id)
        .name(getReportName(analysisId, id))
        .reportType(name())
        .title(title)
        .description(description)
        .reportType(REPORT_TYPE)
        .build();
  }

  private String getReportName(String analysisId, String id) {
    return "analysis/" + analysisId + "/definitions/METRICS/" + id + "/reports";
  }

  private static class Constants {

    // For the simulation we need to take all alerts, we will have only 6 months data retention so
    // 10 years looks like a safe margin.
    private static final int SIMULATION_RANGE_YEARS = 10;
  }
}
