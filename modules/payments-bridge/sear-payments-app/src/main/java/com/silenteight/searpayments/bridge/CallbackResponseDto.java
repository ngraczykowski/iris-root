package com.silenteight.searpayments.bridge;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.io.Serializable;
import javax.validation.constraints.NotNull;

@RequiredArgsConstructor
@Value
class CallbackResponseDto implements Serializable {

  long alertId;

  @NotNull
  String nextStatus;
}
