package com.silenteight.payments.bridge.firco.callback;

import lombok.NonNull;

import com.silenteight.payments.bridge.common.dto.output.ClientRequestDto;

class NoCallbackRequestFactoryImpl implements CallbackRequestFactory {

  @Override
  public CallbackRequest create(@NonNull ClientRequestDto clientRequestDto) {
    return new NoCallbackRequestImpl();
  }
}
