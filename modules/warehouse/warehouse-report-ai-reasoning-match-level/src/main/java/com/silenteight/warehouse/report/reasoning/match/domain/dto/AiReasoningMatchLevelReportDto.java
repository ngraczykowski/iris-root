package com.silenteight.warehouse.report.reasoning.match.domain.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import com.silenteight.warehouse.report.reporting.ReportRange;

@Value
@Builder
public class AiReasoningMatchLevelReportDto {

  @NonNull
  String fileStorageName;
  @NonNull
  ReportRange range;
  @NonNull
  String timestamp;
}
