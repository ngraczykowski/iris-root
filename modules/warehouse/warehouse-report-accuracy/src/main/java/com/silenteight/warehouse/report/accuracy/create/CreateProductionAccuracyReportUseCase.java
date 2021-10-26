package com.silenteight.warehouse.report.accuracy.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.accuracy.domain.AccuracyReportService;
import com.silenteight.warehouse.report.accuracy.generation.AccuracyReportDefinitionProperties;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.LocalDate;
import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;

@RequiredArgsConstructor
class CreateProductionAccuracyReportUseCase {

  private static final String PRODUCTION_ANALYSIS_NAME = "production";

  @NonNull
  private final AccuracyReportService reportService;
  @Valid
  @NonNull
  private final AccuracyReportDefinitionProperties productionProperties;
  @NonNull
  private final IndexesQuery productionIndexerQuery;

  ReportInstanceReferenceDto createReport(LocalDate from, LocalDate to) {
    ReportRange range = of(from, to);
    List<String> indexes = productionIndexerQuery.getIndexesForAnalysis(PRODUCTION_ANALYSIS_NAME);
    return reportService.createReportInstance(range, indexes, productionProperties);
  }
}
