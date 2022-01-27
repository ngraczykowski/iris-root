package com.silenteight.warehouse.report.name;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.warehouse.common.domain.ReportConstants;

@RequiredArgsConstructor
class SimulationReportFileNameService implements ReportFileName {

  private static final String REPORT_TYPE_PLACEHOLDER = "[reportType]";
  private static final String ANALYSIS_ID_PLACEHOLDER = "[analysisId]";
  private static final String TIMESTAMP_PLACEHOLDER = "[timestamp]";

  @NonNull
  private final String fileNamePattern;

  @Override
  public String getReportName(ReportFileNameDto fileNameDto) {
    return fileNamePattern
        .replace(REPORT_TYPE_PLACEHOLDER, fileNameDto.getReportType())
        .replace(ANALYSIS_ID_PLACEHOLDER, fileNameDto.getAnalysisId())
        .replace(TIMESTAMP_PLACEHOLDER, fileNameDto.getTimestamp());
  }

  @Override
  public String type() {
    return ReportConstants.SIMULATION;
  }
}
