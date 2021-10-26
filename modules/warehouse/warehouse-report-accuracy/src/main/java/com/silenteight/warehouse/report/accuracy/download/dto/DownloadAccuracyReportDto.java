package com.silenteight.warehouse.report.accuracy.download.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.InputStream;

@Value
@Builder
public class DownloadAccuracyReportDto {

  @NonNull
  String name;
  @NonNull
  InputStream content;
}
