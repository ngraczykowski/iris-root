package com.silenteight.warehouse.report.reporting;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.InputStream;

@Value
@Builder
class InMemoryReportDto implements Report {

  @NonNull
  String reportName;
  @NonNull
  InputStream inputStream;
}
