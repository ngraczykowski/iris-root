package com.silenteight.warehouse.report.billing.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.billing.domain.BillingReportService;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.LocalDate;
import java.util.List;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;
import static java.lang.String.format;

@RequiredArgsConstructor
class CreateBillingReportUseCase {

  private static final String PRODUCTION_ANALYSIS_NAME = "production";
  private static final String FILE_NAME = "Billing_%s_To_%s.csv";

  @NonNull
  private final BillingReportService reportService;
  @NonNull
  private final IndexesQuery productionIndexerQuery;

  ReportInstanceReferenceDto createProductionReport(LocalDate from, LocalDate to) {
    ReportRange range = of(from, to);
    List<String> indexes = productionIndexerQuery.getIndexesForAnalysis(PRODUCTION_ANALYSIS_NAME);
    String fileName = getFileName(from, to);
    return reportService.createReportInstance(range, fileName, indexes);
  }

  private static String getFileName(LocalDate from, LocalDate to) {
    return format(FILE_NAME, from, to);
  }
}
