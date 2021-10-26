package com.silenteight.warehouse.report.reasoning.match.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.reasoning.match.domain.AiReasoningMatchLevelReportService;
import com.silenteight.warehouse.report.reasoning.match.generation.AiReasoningReportDefinitionProperties;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.reasoning.match.domain.AiReasoningMatchLevelReportDefinition.getReportType;

@RequiredArgsConstructor
public class CreateProductionAiReasoningMatchLevelReportUseCase {

  private static final String PRODUCTION_ANALYSIS_NAME = "production";

  @NonNull
  private final AiReasoningMatchLevelReportService reportService;
  @Valid
  @NonNull
  private final AiReasoningReportDefinitionProperties productionProperties;
  @NonNull
  private final IndexesQuery productionMatchIndexingQuery;

  ReportInstanceReferenceDto createReport(String reportId) {
    List<String> indexes = productionMatchIndexingQuery
        .getIndexesForAnalysis(PRODUCTION_ANALYSIS_NAME);
    return reportService.createReportInstance(
        getReportType(reportId), PRODUCTION_ANALYSIS_NAME, indexes, productionProperties);
  }
}
