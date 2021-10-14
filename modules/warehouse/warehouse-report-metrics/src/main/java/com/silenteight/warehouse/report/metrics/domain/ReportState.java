package com.silenteight.warehouse.report.metrics.domain;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportStatus;

import java.util.function.Function;

@RequiredArgsConstructor
public enum ReportState {
  NEW(ReportStatus::buildReportStatusGenerating),
  GENERATING(ReportStatus::buildReportStatusGenerating),
  DONE(ReportStatus::buildReportStatusOk),
  FAILED(ReportStatus::buildReportStatusFailed);

  private final Function<String, ReportStatus> statusFunction;

  public ReportStatus getReportStatus(@NonNull String reportName) {
    return statusFunction.apply(reportName);
  }
}
