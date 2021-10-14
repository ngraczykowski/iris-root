package com.silenteight.warehouse.report.metrics.v1.domain.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public class ReportDto {

  String filename;
  String content;
}
