package com.silenteight.warehouse.report.rbs.download.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class DownloadRbsReportDto {

  @NonNull
  String name;
  @NonNull
  String content;
}
