package com.silenteight.warehouse.report.name;

import lombok.Builder;
import lombok.Value;

import static org.apache.commons.lang3.StringUtils.EMPTY;

@Value
@Builder
public class ReportFileNameDto {

  @Builder.Default
  String reportType = EMPTY;
  @Builder.Default
  String analysisId = EMPTY;
  @Builder.Default
  String timestamp = EMPTY;
  @Builder.Default
  String from = EMPTY;
  @Builder.Default
  String to = EMPTY;
  @Builder.Default
  String extension = EMPTY;
}
