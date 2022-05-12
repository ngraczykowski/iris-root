package com.silenteight.warehouse.report.reporting;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.nio.file.Path;

@Value
@Builder
public class TempFileReportDto implements Report {

  @NonNull
  String reportName;
  @NonNull
  Path reportPath;
  @NonNull
  Long reportId;
}
