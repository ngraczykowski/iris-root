package com.silenteight.warehouse.report.reasoning.download.dto;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.InputStream;

@Value
@Builder
public class DownloadAiReasoningReportDto {

  @NonNull
  String name;
  @NonNull
  InputStream content;
}
