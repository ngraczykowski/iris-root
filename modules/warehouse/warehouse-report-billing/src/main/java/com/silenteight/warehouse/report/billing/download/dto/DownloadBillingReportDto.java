package com.silenteight.warehouse.report.billing.download.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class DownloadBillingReportDto {

  @NonNull
  String name;
  @NonNull
  String content;
}
