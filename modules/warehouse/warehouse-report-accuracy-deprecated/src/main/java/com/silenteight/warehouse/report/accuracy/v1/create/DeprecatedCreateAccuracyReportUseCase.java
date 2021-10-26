package com.silenteight.warehouse.report.accuracy.v1.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.accuracy.v1.domain.DeprecatedAccuracyReportService;
import com.silenteight.warehouse.report.accuracy.v1.generation.AccuracyReportDefinitionProperties;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.accuracy.v1.domain.DeprecatedAccuracyReportDefinition.SIMULATION;
import static com.silenteight.warehouse.report.accuracy.v1.domain.DeprecatedAccuracyReportDefinition.getReportType;

@RequiredArgsConstructor
public class DeprecatedCreateAccuracyReportUseCase {

  private static final String PRODUCTION_ANALYSIS_NAME = "production";

  @NonNull
  private final DeprecatedAccuracyReportService reportService;
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
