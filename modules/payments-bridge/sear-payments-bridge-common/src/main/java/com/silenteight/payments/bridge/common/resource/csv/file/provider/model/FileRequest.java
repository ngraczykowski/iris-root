package com.silenteight.payments.bridge.common.resource.csv.file.provider.model;

import lombok.*;
import lombok.Builder.Default;

import javax.annotation.Nullable;

@Value
@Builder
@AllArgsConstructor
public class FileRequest {

  @NonNull
  @Default
  String bucket = "";

  @NonNull
  @Default
  String object = "";

  @Getter(onMethod_ = @Nullable)
  String region;
}
