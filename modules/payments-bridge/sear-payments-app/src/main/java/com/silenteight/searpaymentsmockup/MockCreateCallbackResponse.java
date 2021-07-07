package com.silenteight.searpaymentsmockup;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
class MockCreateCallbackResponse implements CreateCallbackResponse {

  private final long alertId;

  @Override
  public CallbackResponseDto invoke() {
    return new CallbackResponseDto(alertId, "nextStatus");
  }
}
