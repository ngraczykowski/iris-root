package com.silenteight.warehouse.report.generation;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.warehouse.report.reporting.Report;

import java.io.InputStream;

@Value
@Builder
class InMemoryReportDto implements Report {

  @NonNull
  String reportName;
  @NonNull
  InputStream inputStream;
}
