package com.silenteight.warehouse.report.reasoning.match.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reasoning.match.domain.dto.AiReasoningMatchLevelReportDto;
import com.silenteight.warehouse.report.reasoning.match.domain.exception.ReportNotFoundException;
import com.silenteight.warehouse.report.reasoning.match.download.AiReasoningMatchLevelReportDataQuery;
import com.silenteight.warehouse.report.reasoning.match.status.AiReasoningMatchLevelReportStatusQuery;
import com.silenteight.warehouse.report.reporting.ReportRange;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
class AiReasoningMatchLevelReportQuery
    implements AiReasoningMatchLevelReportDataQuery, AiReasoningMatchLevelReportStatusQuery {

  @NonNull
  private final AiReasoningMatchLevelReportRepository repository;

  @Override
  public AiReasoningMatchLevelReportDto getAiReasoningReportDto(long id) {
    return ofNullable(repository.getById(id))
        .map(AiReasoningMatchLevelReportQuery::toAiReasoningMatchLevelReportDto)
        .orElseThrow(() -> new ReportNotFoundException(id));
  }

  private static AiReasoningMatchLevelReportDto toAiReasoningMatchLevelReportDto(
      AiReasoningMatchLevelReport report) {

    return AiReasoningMatchLevelReportDto.builder()
        .fileStorageName(report.getFileStorageName())
        .range(toReportRange(report))
        .build();
  }

  private static ReportRange toReportRange(AiReasoningMatchLevelReport report) {
    return of(report.getFrom(), report.getTo());
  }

  @Override
  public ReportState getReportGeneratingState(long id) {
    return ofNullable(repository.getById(id))
        .map(AiReasoningMatchLevelReport::getState)
        .orElseThrow(() -> new ReportNotFoundException(id));
  }
}
