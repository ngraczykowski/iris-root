package com.silenteight.payments.bridge.firco.callback;

import lombok.NonNull;

import com.silenteight.payments.bridge.firco.dto.output.AlertRecommendationDto;

class NoCallbackRequestFactoryImpl implements CallbackRequestFactory {

  @Override
  public CallbackRequest create(@NonNull AlertRecommendationDto recommendationDto) {
    return new NoCallbackRequestImpl();
  }
}
