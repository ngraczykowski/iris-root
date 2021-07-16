package com.silenteight.warehouse.report.billing.domain.dto;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
public class ReportDto {

  @NonNull
  String filename;
  String content;
}
