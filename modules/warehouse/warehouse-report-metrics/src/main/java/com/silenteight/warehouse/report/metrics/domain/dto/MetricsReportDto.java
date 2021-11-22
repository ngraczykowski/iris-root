package com.silenteight.warehouse.report.metrics.domain.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.warehouse.report.reporting.ReportRange;

@Value(staticConstructor = "of")
@Builder
public class MetricsReportDto {

  @NonNull
  ReportRange range;
  @NonNull
  String content;
  @NonNull
  String timestamp;
}
