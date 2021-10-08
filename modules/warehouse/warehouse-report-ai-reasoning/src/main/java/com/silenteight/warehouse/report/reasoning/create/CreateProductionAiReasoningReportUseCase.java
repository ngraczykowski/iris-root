package com.silenteight.warehouse.report.reasoning.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.reasoning.domain.AiReasoningReportService;
import com.silenteight.warehouse.report.reasoning.generation.AiReasoningReportDefinitionProperties;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.reasoning.domain.AiReasoningReportDefinition.getReportType;

@RequiredArgsConstructor
public class CreateProductionAiReasoningReportUseCase {

  private static final String PRODUCTION_ANALYSIS_NAME = "production";

  @NonNull
  private final AiReasoningReportService reportService;
  @Valid
  @NonNull
  private final AiReasoningReportDefinitionProperties productionProperties;
  @NonNull
  private final IndexesQuery productionIndexerQuery;

  ReportInstanceReferenceDto createReport(String reportId) {
    List<String> indexes = productionIndexerQuery.getIndexesForAnalysis(PRODUCTION_ANALYSIS_NAME);
    return reportService.createReportInstance(
        getReportType(reportId), PRODUCTION_ANALYSIS_NAME, indexes, productionProperties);
  }
}
