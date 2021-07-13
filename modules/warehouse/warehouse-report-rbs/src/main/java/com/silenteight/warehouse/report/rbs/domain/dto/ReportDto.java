package com.silenteight.warehouse.report.rbs.domain.dto;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
public class ReportDto {

  @NonNull
  String filename;
  byte[] content;
}
