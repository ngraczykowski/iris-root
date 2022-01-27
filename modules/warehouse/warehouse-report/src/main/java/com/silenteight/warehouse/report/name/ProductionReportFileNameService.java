package com.silenteight.warehouse.report.name;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.domain.ReportConstants;

@RequiredArgsConstructor
class ProductionReportFileNameService implements ReportFileName {

  private static final String REPORT_TYPE_PLACEHOLDER = "[reportType]";
  private static final String FROM_PLACEHOLDER = "[from]";
  private static final String TO_PLACEHOLDER = "[to]";
  private static final String TIMESTAMP_PLACEHOLDER = "[timestamp]";

  @NonNull
  private final String fileNamePattern;

  @Override
  public String getReportName(ReportFileNameDto fileNameDto) {
    return fileNamePattern
        .replace(REPORT_TYPE_PLACEHOLDER, fileNameDto.getReportType())
        .replace(FROM_PLACEHOLDER, fileNameDto.getFrom())
        .replace(TO_PLACEHOLDER, fileNameDto.getTo())
        .replace(TIMESTAMP_PLACEHOLDER, fileNameDto.getTimestamp());
  }

  @Override
  public String type() {
    return ReportConstants.PRODUCTION;
  }
}
