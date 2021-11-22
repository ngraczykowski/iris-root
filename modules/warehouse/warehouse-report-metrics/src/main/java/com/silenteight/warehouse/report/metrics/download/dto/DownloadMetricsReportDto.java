package com.silenteight.warehouse.report.metrics.download.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class DownloadMetricsReportDto {

  @NonNull
  String name;
  @NonNull
  String content;
}
