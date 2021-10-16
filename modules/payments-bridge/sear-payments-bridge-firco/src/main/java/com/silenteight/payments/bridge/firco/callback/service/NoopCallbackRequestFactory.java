package com.silenteight.payments.bridge.firco.callback.service;

import lombok.NonNull;

import com.silenteight.payments.bridge.common.dto.output.ClientRequestDto;

class NoopCallbackRequestFactory implements CallbackRequestFactory {

  @Override
  public CallbackRequest create(@NonNull ClientRequestDto clientRequestDto) {
    return new NoopCallbackRequest();
  }
}
