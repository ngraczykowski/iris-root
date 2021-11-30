package com.silenteight.payments.bridge.svb.newlearning.domain;

import lombok.*;
import lombok.Builder.Default;

@Value
@Builder
@AllArgsConstructor
public class DeleteLearningFileRequest {
  @NonNull
  @Default
  String bucket = "";

  @NonNull
  @Default
  String object = "";
}
