package com.silenteight.warehouse.report.rbs.domain.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.warehouse.report.reporting.ReportRange;

@Value
@Builder
public class RbsReportDto {

  @NonNull
  String content;
  @NonNull
  ReportRange range;
  @NonNull
  String timestamp;
}
