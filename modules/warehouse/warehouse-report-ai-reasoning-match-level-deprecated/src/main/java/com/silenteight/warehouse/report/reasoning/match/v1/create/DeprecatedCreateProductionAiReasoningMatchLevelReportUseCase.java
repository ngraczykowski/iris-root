package com.silenteight.warehouse.report.reasoning.match.v1.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.indexer.query.IndexesQuery;
import com.silenteight.warehouse.report.reasoning.match.v1.domain.DeprecatedAiReasoningMatchLevelReportService;
import com.silenteight.warehouse.report.reasoning.match.v1.generation.AiReasoningReportDefinitionProperties;
import com.silenteight.warehouse.report.reporting.ReportInstanceReferenceDto;

import java.util.List;
import javax.validation.Valid;

import static com.silenteight.warehouse.report.reasoning.match.v1.domain.DeprecatedAiReasoningMatchLevelReportDefinition.getReportType;

@RequiredArgsConstructor
public class DeprecatedCreateProductionAiReasoningMatchLevelReportUseCase {

  private static final String PRODUCTION_ANALYSIS_NAME = "production";

  @NonNull
  private final DeprecatedAiReasoningMatchLevelReportService reportService;
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
