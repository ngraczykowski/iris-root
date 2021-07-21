package com.silenteight.warehouse.report.sm.domain;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportsDefinitionListDto.ReportDefinitionDto;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;

@RequiredArgsConstructor
@Getter
public enum ReportDefinition {
  SIMULATION_METRICS(
      "1acb8a9f-c560-4b5c-95a3-c69bcf32b22e",
      "simulation-metrics.csv",
      "Simulation - metrics",
      "Simulation metrics report");

  @NonNull
  private final String id;
  @NonNull
  private final String filename;
  @NonNull
  private final String title;
  @NonNull
  private final String description;

  public static List<ReportDefinitionDto> toReportsDefinitionDto(String analysisId) {
    return stream(ReportDefinition.values())
        .map(reportDefinition -> reportDefinition.toReportDefinitionDto(analysisId))
        .collect(Collectors.toList());
  }

  private ReportDefinitionDto toReportDefinitionDto(String analysisId) {
    return ReportDefinitionDto
        .builder()
        .id(id)
        .name(getReportName(analysisId, id))
        .reportType(name())
        .title(title)
        .description(description)
        .build();
  }

  private String getReportName(String analysisId, String id) {
    return "analysis/" + analysisId + "/definitions/SIMULATION_METRICS/" + id + "/reports";
  }
}
