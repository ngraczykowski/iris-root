package com.silenteight.warehouse.report.metrics.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.metrics.generation.PropertiesDefinition;
import com.silenteight.warehouse.report.remove.ReportsRemoval;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import java.time.OffsetDateTime;
import java.util.List;
import javax.validation.Valid;

@Slf4j
@RequiredArgsConstructor
public class DeprecatedMetricsReportService implements ReportsRemoval {

  private static final String PRODUCTION_ANALYSIS_NAME = "production";
  @NonNull
  private final DeprecatedMetricsReportRepository repository;
  @NonNull
  private final DeprecatedAsyncMetricsReportGenerationService asyncReportGenerationService;
  @NonNull
  private final IndexesQuery productionIndexerQuery;
  @NonNull
  private final IndexesQuery simulationIndexerQuery;
  @Valid
  @NonNull
  private final PropertiesDefinition productionProperties;
  @Valid
  @NonNull
  private final PropertiesDefinition simulationProperties;

  public ReportInstanceReferenceDto createProductionReportInstance(
      DeprecatedMetricsReportDefinition definition, String analysisId) {

    return createReportInstance(
        definition,
        analysisId,
        productionIndexerQuery.getIndexesForAnalysis(PRODUCTION_ANALYSIS_NAME),
        productionProperties);
  }

  public ReportInstanceReferenceDto createSimulationReportInstance(
      DeprecatedMetricsReportDefinition definition, String analysisId) {

    return createReportInstance(
        definition,
        analysisId,
        simulationIndexerQuery.getIndexesForAnalysis(analysisId),
        simulationProperties);
  }

  private ReportInstanceReferenceDto createReportInstance(
      DeprecatedMetricsReportDefinition definition,
      String analysisId,
      List<String> indexesForAnalysis,
      PropertiesDefinition simulationProperties) {

    DeprecatedMetricsReport report = DeprecatedMetricsReport.of(definition, analysisId);
    DeprecatedMetricsReport savedReport = repository.save(report);
    asyncReportGenerationService.generateReport(
        savedReport.getId(), indexesForAnalysis, simulationProperties);

    return new ReportInstanceReferenceDto(savedReport.getId());
  }

  public void removeReport(long id) {
    repository.deleteById(id);
  }

  @Override
  public long removeOlderThan(OffsetDateTime dayToRemoveReports) {
    long numberOfRemovedReports = repository.removeAllByCreatedAtBefore(dayToRemoveReports);
    log.info(
        "Number of removed Metrics reports reportsCount={}", numberOfRemovedReports);
    return numberOfRemovedReports;
  }
}
