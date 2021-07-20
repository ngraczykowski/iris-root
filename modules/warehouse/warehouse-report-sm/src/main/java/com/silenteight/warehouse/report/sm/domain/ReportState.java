package com.silenteight.warehouse.report.sm.domain;

import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.report.reporting.ReportStatus;

import java.util.function.Function;

@RequiredArgsConstructor
public enum ReportState {
  NEW(ReportStatus::buildReportStatusGenerating),
  GENERATING(ReportStatus::buildReportStatusGenerating),
  DONE(ReportStatus::buildReportStatusOk);

  private final Function<String, ReportStatus> statusFunction;

  public ReportStatus getReportStatus(String reportName) {
    return statusFunction.apply(reportName);
  }
}
