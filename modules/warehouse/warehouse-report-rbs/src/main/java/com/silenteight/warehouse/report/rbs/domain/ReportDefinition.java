package com.silenteight.warehouse.report.rbs.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.DefaultTimeSource;
import com.silenteight.warehouse.report.rbs.domain.exception.ReportTypeNotFoundException;
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
public enum ReportDefinition {

  DAY(
      "f21af598-c78a-4e3a-a3ac-04e1f3d11ea4",
      "rb-scorer-1-day.csv",
      "RB Scorer - Last day",
      "RB Scorer report - Last day",
      ofDays(1),
      true),
  WEEK(
      "ddce27a5-4560-4dc7-83c3-b4e618986c03",
      "rb-scorer-7-days.csv",
      "RB Scorer - Last 7 days",
      "RB Scorer report - Last 7 days",
      ofDays(7),
      true),
  MONTH(
      "df7b3309-8f25-4aae-ae5c-48e174332d1a",
      "rb-scorer-30-days.csv",
      "RB Scorer - Last 30 days",
      "RB Scorer report - Last 30 days",
      ofDays(30),
      true),
  THREE_MONTHS(
      "87834a2e-abf8-4b0a-a35a-2c109404a43b",
      "rb-scorer-90-days.csv",
      "RB Scorer - Last 90 days",
      "RB Scorer report - Last 90 days",
      ofDays(90),
      true),
  SIMULATION(
      "6bbb75dd-8626-4384-bcfa-f8e83ee9f040",
      "rbscorer.csv",
      "RB Scorer",
      "Simulation Reasoning Branch Scorer",
      ofYears(Constants.SIMULATION_RANGE_YEARS),
      false);

  private static final String REPORT_TYPE = "RB_SCORER";
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


  public static List<ReportDefinitionDto> toReportsDefinitionDto() {
    return stream(ReportDefinition.values())
        .filter(ReportDefinition::isProduction)
        .map(ReportDefinition::toReportDefinitionDto)
        .collect(toList());
  }

  public static ReportDefinition getReportType(String id) {
    return stream(values())
        .filter(reportDefinition -> reportDefinition.hasId(id))
        .findAny()
        .orElseThrow(() -> new ReportTypeNotFoundException(id));
  }

  private ReportDefinitionDto toReportDefinitionDto() {
    return ReportDefinitionDto
        .builder()
        .id(id)
        .name(getReportName("production", id))
        .title(title)
        .description(description)
        .reportType(REPORT_TYPE)
        .build();
  }

  OffsetDateTime getFrom(Instant now) {
    return DefaultTimeSource.TIME_CONVERTER.toOffset(now).minus(reportRange);
  }

  OffsetDateTime getTo(Instant now) {
    return DefaultTimeSource.TIME_CONVERTER.toOffset(now);
  }

  private boolean hasId(String id) {
    return this.getId().equals(id);
  }

  public static List<ReportDefinitionDto> toReportsDefinitionDto(String analysisId) {
    return stream(ReportDefinition.values())
        .filter(reportDefinition1 -> !reportDefinition1.isProduction())
        .map(reportDefinition -> reportDefinition.toSimulationReportsDefinitionDto(analysisId))
        .collect(toList());
  }

  private ReportDefinitionDto toSimulationReportsDefinitionDto(String analysisId) {
    return ReportDefinitionDto
        .builder()
        .id(id)
        .reportType(name())
        .name(getReportName(analysisId, id))
        .title(title)
        .description(description)
        .build();
  }

  private String getReportName(String analysisId, String id) {
    return "analysis/" + analysisId + "/definitions/RB_SCORER/" + id + "/reports";
  }

  private static class Constants {

    // For the simulation we need to take all alerts, we will have only 6 months data retention so
    // 10 years looks like a safe margin.
    private static final int SIMULATION_RANGE_YEARS = 10;
  }
}
