package com.silenteight.sens.webapp.report.list;

import lombok.Builder;
import lombok.Builder.Default;
import lombok.NonNull;
import lombok.Value;

import com.fasterxml.jackson.annotation.JsonProperty;

@Builder
@Value
public class ReportDto {

  @NonNull
  String name;
  @NonNull
  String type;
  @NonNull
  @JsonProperty("title")
  String label;
  @NonNull
  FilterDto filter;
  @NonNull
  @Default
  DownloadType download = DownloadType.SYNC;
}
