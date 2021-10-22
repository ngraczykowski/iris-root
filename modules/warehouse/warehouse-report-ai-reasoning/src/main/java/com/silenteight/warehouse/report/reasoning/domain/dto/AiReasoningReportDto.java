package com.silenteight.warehouse.report.reasoning.domain.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.warehouse.report.reporting.ReportRange;

@Value
@Builder
public class AiReasoningReportDto {

  @NonNull
  String fileStorageName;
  @NonNull
  ReportRange range;
}
