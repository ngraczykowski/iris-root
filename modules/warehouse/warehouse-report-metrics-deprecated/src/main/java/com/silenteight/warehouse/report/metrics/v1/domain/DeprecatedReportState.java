package com.silenteight.warehouse.report.metrics.v1.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportStatus;

import java.util.function.Function;

@RequiredArgsConstructor
public enum DeprecatedReportState {
  NEW(ReportStatus::buildReportStatusGenerating),
  GENERATING(ReportStatus::buildReportStatusGenerating),
  DONE(ReportStatus::buildReportStatusOk),
  FAILED(ReportStatus::buildReportStatusFailed);

  private final Function<String, ReportStatus> statusFunction;

  public ReportStatus getReportStatus(String reportName) {
    return statusFunction.apply(reportName);
  }
}
