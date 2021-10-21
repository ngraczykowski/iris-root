package com.silenteight.warehouse.report.rbs.domain.dto;

import lombok.Value;

@Value(staticConstructor = "of")
public class ReportDto {

  String filename;
  String content;
}
