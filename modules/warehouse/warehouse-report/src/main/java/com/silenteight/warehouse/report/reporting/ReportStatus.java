package com.silenteight.warehouse.report.reporting;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import javax.annotation.Nullable;


@Value
@Builder
public class ReportStatus {

  @NonNull
  Status status;

  @Nullable
  String downloadReportUrl;

  public static ReportStatus buildReportStatusOk(String downloadReportUrl) {
    return ReportStatus.builder()
        .status(Status.OK)
        .downloadReportUrl(downloadReportUrl)
        .build();
  }

  public static ReportStatus buildReportStatusGenerating() {
    return ReportStatus.builder()
        .status(Status.GENERATING)
        .build();
  }

  private enum Status {
    OK,
    GENERATING
  }
}
