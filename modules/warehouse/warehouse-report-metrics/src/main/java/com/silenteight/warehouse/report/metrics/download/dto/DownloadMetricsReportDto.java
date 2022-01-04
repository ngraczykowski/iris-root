package com.silenteight.warehouse.report.metrics.download.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.InputStream;

@Value
@Builder
public class DownloadMetricsReportDto {

  @NonNull
  String name;
  @NonNull
  InputStream content;
}
