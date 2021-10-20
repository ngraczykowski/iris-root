package com.silenteight.payments.bridge.svb.learning.reader.domain;

import lombok.*;
import lombok.Builder.Default;

import javax.annotation.Nullable;

@Value
@Builder
@AllArgsConstructor
public class LearningRequest {

  @NonNull
  @Default
  String bucket = "";

  @NonNull
  @Default
  String object = "";

  @Getter(onMethod_ = @Nullable)
  String region;
}
