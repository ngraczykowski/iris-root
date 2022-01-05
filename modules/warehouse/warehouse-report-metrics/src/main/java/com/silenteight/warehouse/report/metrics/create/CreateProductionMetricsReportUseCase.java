package com.silenteight.warehouse.report.metrics.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.metrics.domain.MetricsReportService;
import com.silenteight.warehouse.report.reporting.PropertiesDefinition;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.OffsetDateTime;
import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;

@RequiredArgsConstructor
class CreateProductionMetricsReportUseCase {

  private static final String PRODUCTION_ANALYSIS_NAME = "production";

  @NonNull
  private final MetricsReportService reportService;
  @NonNull
  private final IndexesQuery productionIndexerQuery;
  @Valid
  @NonNull
  private final PropertiesDefinition productionProperties;

  ReportInstanceReferenceDto createReport(OffsetDateTime from, OffsetDateTime to) {
    ReportRange range = of(from, to);
    List<String> indexes = productionIndexerQuery.getIndexesForAnalysis(PRODUCTION_ANALYSIS_NAME);
    return reportService.createReportInstance(range, indexes, productionProperties);
  }
}
