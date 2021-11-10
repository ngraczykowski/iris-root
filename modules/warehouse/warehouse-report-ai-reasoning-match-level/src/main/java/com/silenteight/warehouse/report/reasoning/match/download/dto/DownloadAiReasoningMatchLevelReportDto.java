package com.silenteight.warehouse.report.reasoning.match.download.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.InputStream;

@Value
@Builder
public class DownloadAiReasoningMatchLevelReportDto {

  @NonNull
  String name;
  @NonNull
  InputStream content;
}
