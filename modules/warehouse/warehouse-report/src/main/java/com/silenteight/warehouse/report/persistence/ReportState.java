package com.silenteight.warehouse.report.persistence;

import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
public enum ReportState {

  NEW(ReportStatus::buildReportStatusGenerating),
  GENERATING(ReportStatus::buildReportStatusGenerating),
  DONE(ReportStatus::buildReportStatusOk),
  FAILED(ReportStatus::buildReportStatusFailed);

  private final Function<String, ReportStatus> statusFunction;

  public ReportStatus getReportStatus(String reportName) {
    return statusFunction.apply(reportName);
  }
}
