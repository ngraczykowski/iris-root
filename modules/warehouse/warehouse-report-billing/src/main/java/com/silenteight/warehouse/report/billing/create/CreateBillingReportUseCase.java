package com.silenteight.warehouse.report.billing.create;

import lombok.AllArgsConstructor;
import lombok.NonNull;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.billing.domain.BillingReportService;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportRange;

import org.springframework.lang.Nullable;

import java.time.OffsetDateTime;
import java.util.List;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;

@AllArgsConstructor
class CreateBillingReportUseCase {

  private static final String PRODUCTION_ANALYSIS_NAME = "production";

  @Nullable
  private final BillingReportService reportService;
  @NonNull
  private final IndexesQuery productionIndexerQuery;

  ReportInstanceReferenceDto createReport(OffsetDateTime from, OffsetDateTime to) {
    ReportRange range = of(from, to);
    List<String> indexes = productionIndexerQuery.getIndexesForAnalysis(PRODUCTION_ANALYSIS_NAME);
    return reportService.createReportInstance(range, indexes);
  }
}
