package com.silenteight.warehouse.report.reasoning.match.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reasoning.match.domain.exception.ReportTypeNotFoundException;
import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.Period;
import java.util.List;

import static com.silenteight.sep.base.common.time.DefaultTimeSource.TIME_CONVERTER;
import static java.time.Period.ofDays;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
@Getter
public enum AiReasoningMatchLevelReportDefinition {

  MONTH(
      "daa38c15-e811-412b-ac31-d7053fdc319d",
      "ai-reasoning-match-level-30-days.csv",
      "AI Reasoning Match Level - Last 30 days",
      "AI Reasoning Match Level report - Last 30 days",
      ofDays(30),
      true);

  private static final String PRODUCTION_ANALYSIS_NAME = "production";
  private static final String REPORT_TYPE = "AI_REASONING_MATCH_LEVEL";
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
    return stream(AiReasoningMatchLevelReportDefinition.values())
        .filter(AiReasoningMatchLevelReportDefinition::isProduction)
        .map(reportDefinition -> reportDefinition.toReportDefinitionDto(PRODUCTION_ANALYSIS_NAME))
        .collect(toList());
  }

  public static AiReasoningMatchLevelReportDefinition getReportType(String id) {
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
}
