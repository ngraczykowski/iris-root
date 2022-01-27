package com.silenteight.warehouse.report.persistence;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;


@Value
@Builder
public class ReportStatus {

  @NonNull
  Status status;
  @NonNull
  String reportName;

  public static ReportStatus buildReportStatusOk(String reportName) {
    return ReportStatus.builder()
        .status(Status.OK)
        .reportName(reportName)
        .build();
  }

  public static ReportStatus buildReportStatusGenerating(String reportName) {
    return ReportStatus.builder()
        .status(Status.GENERATING)
        .reportName(reportName)
        .build();
  }

  public static ReportStatus buildReportStatusFailed(String reportName) {
    return ReportStatus.builder()
        .status(Status.FAILED)
        .reportName(reportName)
        .build();
  }

  private enum Status {
    OK,
    GENERATING,
    FAILED
  }
}
