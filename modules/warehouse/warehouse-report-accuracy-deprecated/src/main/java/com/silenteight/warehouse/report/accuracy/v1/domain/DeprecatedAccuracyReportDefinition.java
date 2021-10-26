package com.silenteight.warehouse.report.accuracy.v1.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.accuracy.v1.domain.exception.ReportTypeNotFoundException;
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
public enum DeprecatedAccuracyReportDefinition {

  DAY(
      "b4219d35-e337-4d1f-af69-9789b325b2a8",
      "accuracy-1-day.csv",
      "Accuracy - Last day",
      "Accuracy report - Last day",
      ofDays(1),
      true),
  WEEK(
      "6e1bf291-3dbc-479f-a799-f18281f3f39b",
      "accuracy-7-days.csv",
      "Accuracy - Last 7 days",
      "Accuracy report - Last 7 days",
      ofDays(7),
      true),
  MONTH(
      "7031a370-fdef-47cd-9042-1d2fb508a3a0",
      "accuracy-30-days.csv",
      "Accuracy - Last 30 days",
      "Accuracy report - Last 30 days",
      ofDays(30),
      true),
  FIVE_MONTHS(
      "f1fe088c-52e8-4f27-9a64-5a5fbfbf80bc",
      "accuracy-151-days.csv",
      "Accuracy - Last 151 days",
      "Accuracy report - Last 151 days",
      ofDays(151),
      true),
  ONE_YEAR(
      "c97c5fba-3cb4-40c5-b59f-42ebe30e62c1",
      "accuracy-1-year.csv",
      "Accuracy - Last year",
      "Accuracy report - Last year",
      ofYears(1),
      true),
  SIMULATION(
      "62244c81-7ca6-4334-b896-ee586b254dca",
      "accuracy.csv",
      "Accuracy",
      "Simulation Accuracy report",
      ofYears(Constants.SIMULATION_RANGE_YEARS),
      false);

  private static final String PRODUCTION_ANALYSIS_NAME = "production";
  private static final String REPORT_TYPE = "ACCURACY";
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
    return stream(DeprecatedAccuracyReportDefinition.values())
        .filter(DeprecatedAccuracyReportDefinition::isProduction)
        .map(reportDefinition -> reportDefinition.toReportDefinitionDto(PRODUCTION_ANALYSIS_NAME))
        .collect(toList());
  }

  public static List<ReportDefinitionDto> toSimulationReportsDefinitionDto(String analysisId) {
    return stream(DeprecatedAccuracyReportDefinition.values())
        .filter(not(DeprecatedAccuracyReportDefinition::isProduction))
        .map(reportDefinition -> reportDefinition.toReportDefinitionDto(analysisId))
        .collect(toList());
  }

  public static DeprecatedAccuracyReportDefinition getReportType(String id) {
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
    return "analysis/" + analysisId + "/definitions/ACCURACY/" + id + "/reports";
  }

  private static class Constants {

    // For the simulation we need to take all alerts, we will have only 6 months data retention so
    // 10 years looks like a safe margin.
    private static final int SIMULATION_RANGE_YEARS = 10;
  }
}
