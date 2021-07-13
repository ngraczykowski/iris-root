package com.silenteight.warehouse.report.rbs.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.rbs.domain.exception.ReportTypeNotFoundException;
import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.List;

import static java.time.Period.ofDays;
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
      ofDays(1)),
  WEEK(
      "ddce27a5-4560-4dc7-83c3-b4e618986c03",
      "rb-scorer-7-days.csv",
      "RB Scorer - Last 7 days",
      "RB Scorer report - Last 7 days",
      ofDays(7)),
  MONTH(
      "df7b3309-8f25-4aae-ae5c-48e174332d1a",
      "rb-scorer-30-days.csv",
      "RB Scorer - Last 30 days",
      "RB Scorer report - Last 30 days",
      ofDays(30)),
  THREE_MONTHS(
      "87834a2e-abf8-4b0a-a35a-2c109404a43b",
      "rb-scorer-90-days.csv",
      "RB Scorer - Last 90 days",
      "RB Scorer report - Last 90 days",
      ofDays(90));

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
  private final TemporalAmount reportRange;


  public static List<ReportDefinitionDto> toReportsDefinitionDto() {
    return stream(ReportDefinition.values())
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
        .name(getReportName(id))
        .title(title)
        .description(description)
        .reportType(REPORT_TYPE)
        .build();
  }

  private String getReportName(String id) {
    return "analysis/production/definitions/RB_SCORER/" + id + "/reports";
  }

  Instant getFrom(Instant now) {
    return now.minus(reportRange);
  }

  Instant getTo(Instant now) {
    return now;
  }

  private boolean hasId(String id) {
    return this.getId().equals(id);
  }
}
