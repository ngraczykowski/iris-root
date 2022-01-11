package com.silenteight.warehouse.report.billing.download.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.InputStream;

@Value
@Builder
public class DownloadBillingReportDto {

  @NonNull
  String name;
  @NonNull
  InputStream content;
}
