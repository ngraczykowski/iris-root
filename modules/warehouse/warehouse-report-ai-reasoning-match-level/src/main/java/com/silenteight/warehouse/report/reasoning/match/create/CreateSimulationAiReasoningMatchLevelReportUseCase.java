package com.silenteight.warehouse.report.reasoning.match.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.sep.base.common.time.TimeSource;
import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.reasoning.match.domain.AiReasoningMatchLevelReportService;
import com.silenteight.warehouse.report.reasoning.match.generation.AiReasoningMatchLevelReportDefinitionProperties;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;
import static java.time.OffsetDateTime.ofInstant;
import static java.time.ZoneOffset.UTC;

@RequiredArgsConstructor
class CreateSimulationAiReasoningMatchLevelReportUseCase {

  @NonNull
  private final AiReasoningMatchLevelReportService reportService;
  @Valid
  @NonNull
  private final AiReasoningMatchLevelReportDefinitionProperties simulationProperties;
  @NonNull
  private final IndexesQuery simulationIndexerQuery;
  @NonNull
  private final TimeSource timeSource;

  ReportInstanceReferenceDto createReport(String analysisId) {
    List<String> indexes = simulationIndexerQuery.getIndexesForAnalysis(analysisId);
    OffsetDateTime offsetDateNow = timeSource.offsetDateTime().withOffsetSameInstant(UTC);
    ReportRange range = of(ofInstant(Instant.EPOCH, UTC), offsetDateNow);
    return reportService.createReportInstance(range, indexes, simulationProperties);
  }
}
