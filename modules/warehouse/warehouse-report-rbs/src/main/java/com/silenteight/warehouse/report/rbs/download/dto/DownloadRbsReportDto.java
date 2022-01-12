package com.silenteight.warehouse.report.rbs.download.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.InputStream;

@Value
@Builder
public class DownloadRbsReportDto {

  @NonNull
  String name;
  @NonNull
  InputStream content;
}
