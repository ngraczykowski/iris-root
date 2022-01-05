package com.silenteight.warehouse.report.reasoning.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.reasoning.domain.AiReasoningReportService;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;
import com.silenteight.warehouse.report.reporting.ReportProperties;
import com.silenteight.warehouse.report.reporting.ReportRange;

import java.time.OffsetDateTime;
import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.reporting.ReportRange.of;

@RequiredArgsConstructor
class CreateProductionAiReasoningReportUseCase {

  private static final String PRODUCTION_ANALYSIS_NAME = "production";

  @NonNull
  private final AiReasoningReportService reportService;
  @Valid
  @NonNull
  private final ReportProperties productionProperties;
  @NonNull
  private final IndexesQuery productionIndexerQuery;

  ReportInstanceReferenceDto createReport(OffsetDateTime from, OffsetDateTime to) {
    ReportRange range = of(from, to);
    List<String> indexes = productionIndexerQuery.getIndexesForAnalysis(PRODUCTION_ANALYSIS_NAME);
    return reportService.createReportInstance(range, indexes,
        productionProperties.getAiReasoning().getProduction());
  }
}
