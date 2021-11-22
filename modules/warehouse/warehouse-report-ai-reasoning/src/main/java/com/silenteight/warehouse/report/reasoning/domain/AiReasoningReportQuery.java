package com.silenteight.warehouse.report.reasoning.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reasoning.domain.dto.AiReasoningReportDto;
import com.silenteight.warehouse.report.reasoning.domain.exception.ReportNotFoundException;
import com.silenteight.warehouse.report.reasoning.download.AiReasoningReportDataQuery;
import com.silenteight.warehouse.report.reasoning.status.AiReasoningReportStatusQuery;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.OffsetDateTime;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;
import static java.lang.String.valueOf;
import static java.time.ZoneOffset.UTC;
import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
class AiReasoningReportQuery implements AiReasoningReportDataQuery, AiReasoningReportStatusQuery {

  @NonNull
  private final AiReasoningReportRepository repository;

  @Override
  public AiReasoningReportDto getAiReasoningReportDto(long id) {
    return ofNullable(repository.getById(id))
        .map(AiReasoningReportQuery::toAiReasoningReportDto)
        .orElseThrow(() -> new ReportNotFoundException(id));
  }

  private static AiReasoningReportDto toAiReasoningReportDto(AiReasoningReport report) {
    return AiReasoningReportDto.builder()
        .fileStorageName(report.getFileStorageName())
        .range(toReportRange(report))
        .timestamp(toTimestamp(report.getCreatedAt()))
        .build();
  }

  private static ReportRange toReportRange(AiReasoningReport report) {
    return of(report.getFrom(), report.getTo());
  }

  private static String toTimestamp(OffsetDateTime createdAt) {
    return valueOf(createdAt.atZoneSameInstant(UTC).toEpochSecond());
  }

  @Override
  public ReportState getReportGeneratingState(long id) {
    return ofNullable(repository.getById(id))
        .map(AiReasoningReport::getState)
        .orElseThrow(() -> new ReportNotFoundException(id));
  }
}
