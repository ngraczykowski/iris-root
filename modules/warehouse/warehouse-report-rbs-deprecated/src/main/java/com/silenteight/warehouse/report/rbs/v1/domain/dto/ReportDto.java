package com.silenteight.warehouse.report.rbs.v1.domain.dto;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
public class ReportDto {

  @NonNull
  String filename;
  String content;
}
