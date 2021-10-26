package com.silenteight.warehouse.report.reasoning.match.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.reasoning.match.domain.AiReasoningMatchLevelReportService;
import com.silenteight.warehouse.report.reasoning.match.generation.AiReasoningReportDefinitionProperties;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.reasoning.match.domain.AiReasoningMatchLevelReportDefinition.SIMULATION;

@RequiredArgsConstructor
public class CreateSimulationAiReasoningMatchLevelReportUseCase {

  @NonNull
  private final AiReasoningMatchLevelReportService reportService;
  @Valid
  @NonNull
  private final AiReasoningReportDefinitionProperties simulationProperties;
  @NonNull
  private final IndexesQuery simulationIndexerQuery;

  ReportInstanceReferenceDto createReport(String analysisId) {
    List<String> indexes = simulationIndexerQuery.getIndexesForAnalysis(analysisId);
    return reportService.createReportInstance(
        SIMULATION, analysisId, indexes, simulationProperties);
  }
}
