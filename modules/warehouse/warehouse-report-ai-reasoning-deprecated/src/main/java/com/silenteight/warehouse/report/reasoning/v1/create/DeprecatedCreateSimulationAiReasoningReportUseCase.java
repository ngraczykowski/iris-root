package com.silenteight.warehouse.report.reasoning.v1.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.reasoning.v1.domain.DeprecatedAiReasoningReportService;
import com.silenteight.warehouse.report.reasoning.v1.generation.AiReasoningReportDefinitionProperties;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.reasoning.v1.domain.DeprecatedAiReasoningReportDefinition.SIMULATION;

@RequiredArgsConstructor
public class DeprecatedCreateSimulationAiReasoningReportUseCase {

  @NonNull
  private final DeprecatedAiReasoningReportService reportService;
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
