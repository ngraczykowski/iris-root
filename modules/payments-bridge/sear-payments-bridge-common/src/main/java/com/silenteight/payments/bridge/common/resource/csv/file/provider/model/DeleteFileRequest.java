package com.silenteight.payments.bridge.common.resource.csv.file.provider.model;

import lombok.*;
import lombok.Builder.Default;

@Value
@Builder
@AllArgsConstructor
public class DeleteFileRequest {
  @NonNull
  @Default
  String bucket = "";

  @NonNull
  @Default
  String object = "";
}
