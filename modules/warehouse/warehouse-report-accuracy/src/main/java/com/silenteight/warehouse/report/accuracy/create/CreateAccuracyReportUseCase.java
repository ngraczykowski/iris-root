package com.silenteight.warehouse.report.accuracy.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.indexing.IndexesQuery;
import com.silenteight.warehouse.report.accuracy.domain.AccuracyReportService;
import com.silenteight.warehouse.report.accuracy.generation.AccuracyReportDefinitionProperties;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.accuracy.domain.AccuracyReportDefinition.SIMULATION;
import static com.silenteight.warehouse.report.accuracy.domain.AccuracyReportDefinition.getReportType;

@RequiredArgsConstructor
public class CreateAccuracyReportUseCase {

  private static final String PRODUCTION_ANALYSIS_NAME = "production";

  @NonNull
  private final AccuracyReportService reportService;
  @Valid
  @NonNull
  private final AccuracyReportDefinitionProperties productionProperties;
  @Valid
  @NonNull
  private final AccuracyReportDefinitionProperties simulationProperties;
  @NonNull
  private final IndexesQuery productionIndexerQuery;
  @NonNull
  private final IndexesQuery simulationIndexerQuery;

  ReportInstanceReferenceDto createSimulationReport(String analysisId) {
    List<String> indexes = simulationIndexerQuery.getIndexesForAnalysis(analysisId);
    return reportService.createReportInstance(
        SIMULATION, analysisId, indexes, simulationProperties);
  }

  ReportInstanceReferenceDto createProductionReport(String reportId) {
    List<String> indexes = productionIndexerQuery.getIndexesForAnalysis(PRODUCTION_ANALYSIS_NAME);
    return reportService.createReportInstance(
        getReportType(reportId), PRODUCTION_ANALYSIS_NAME, indexes, productionProperties);
  }
}
