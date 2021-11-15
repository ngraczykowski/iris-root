package com.silenteight.warehouse.report.metrics.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.metrics.domain.MetricsReportService;
import com.silenteight.warehouse.report.metrics.generation.PropertiesDefinition;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.OffsetDateTime;
import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;
import static java.lang.String.format;

@RequiredArgsConstructor
class CreateProductionMetricsReportUseCase {

  private static final String PRODUCTION_ANALYSIS_NAME = "production";
  private static final String FILE_NAME = "Production_Metrics_%s_To_%s.csv";

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
    String fileName = getFileName(range);
    return reportService.createReportInstance(range, fileName, indexes, productionProperties);
  }

  private static String getFileName(ReportRange range) {
    return format(FILE_NAME, range.getFromAsLocalDate(), range.getToAsLocalDate());
  }
}
