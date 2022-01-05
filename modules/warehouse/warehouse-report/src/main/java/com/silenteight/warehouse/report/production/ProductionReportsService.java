package com.silenteight.warehouse.report.production;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reporting.*;
import com.silenteight.warehouse.report.reporting.ReportTypeListDto.ReportTypeDto;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProductionReportsService {

  private static final String ACCURACY = "ACCURACY";
  private static final String AI_REASONING = "AI_REASONING";
  private static final String AI_REASONING_MATCH_LEVEL = "AI_REASONING_MATCH_LEVEL";
  private static final String BILLING = "BILLING";
  private static final String METRICS = "METRICS";
  private static final String RB_SCORER = "RB_SCORER";

  private static final String ACCURACY_TITLE = "Accuracy";
  private static final String AI_REASONING_TITLE = "AI Reasoning";
  private static final String AI_REASONING_MATCH_LEVEL_TITLE = "AI Reasoning Match Level";
  private static final String BILLING_TITLE = "Billing";
  private static final String METRICS_TITLE = "Metrics";
  private static final String RB_SCORER_TITLE = "RB Scorer";

  private static final String PRODUCTION_NAME = "analysis/production/reports/";

  private final ReportProperties reportProperties;

  public List<ReportTypeDto> getListOfReports() {
    List<ReportTypeDto> availableReports = new ArrayList<>();

    if (Optional.ofNullable(reportProperties.getAccuracy())
        .map(AccuracyReportProperties::getProduction).isPresent()) {
      availableReports.add(ReportTypeDto.builder()
          .type(ACCURACY)
          .title(ACCURACY_TITLE)
          .name(PRODUCTION_NAME + ACCURACY).build());
    }

    if (Optional.ofNullable(reportProperties.getAiReasoning())
        .map(AiReasoningReportProperties::getProduction).isPresent()) {
      availableReports.add(ReportTypeDto.builder()
          .type(AI_REASONING)
          .title(AI_REASONING_TITLE)
          .name(PRODUCTION_NAME + AI_REASONING).build());
    }

    if (Optional.ofNullable(reportProperties.getAiReasoningMatchLevel())
        .map(AiReasoningMatchLevelReportProperties::getProduction).isPresent()) {
      availableReports.add(ReportTypeDto.builder()
          .type(AI_REASONING_MATCH_LEVEL)
          .title(AI_REASONING_MATCH_LEVEL_TITLE)
          .name(PRODUCTION_NAME + AI_REASONING_MATCH_LEVEL).build());
    }

    if (Optional.ofNullable(reportProperties.getBilling()).isPresent()) {
      availableReports.add(ReportTypeDto.builder()
          .type(BILLING)
          .title(BILLING_TITLE)
          .name(PRODUCTION_NAME + BILLING).build());
    }

    if (Optional.ofNullable(reportProperties.getMetrics())
        .map(MetricsReportProperties::getProduction).isPresent()) {
      availableReports.add(ReportTypeDto.builder()
          .type(METRICS)
          .title(METRICS_TITLE)
          .name(PRODUCTION_NAME + METRICS).build());
    }

    if (Optional.ofNullable(reportProperties.getRbs())
        .map(RbsReportProperties::getProduction).isPresent()) {
      availableReports.add(ReportTypeDto.builder()
          .type(RB_SCORER)
          .title(RB_SCORER_TITLE)
          .name(PRODUCTION_NAME + RB_SCORER).build());
    }

    return availableReports;
  }
}
