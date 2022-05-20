package com.silenteight.warehouse.report.download;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.InputStream;

@Value
@Builder
class DownloadReportDto {

  @NonNull
  String name;
  @NonNull
  InputStream content;
}
