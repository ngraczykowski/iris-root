package com.silenteight.payments.bridge.common.resource.csv.file.provider.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class FilesListPatternRequest {

  @NonNull
  @Default
  String bucket = "";

  @NonNull
  @Default
  String filePattern = "";
}
