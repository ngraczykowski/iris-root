package com.silenteight.payments.bridge.firco.callback;

import lombok.NonNull;

import com.silenteight.payments.bridge.firco.dto.output.AlertRecommendationDto;

interface CallbackRequestFactory {

  CallbackRequest create(@NonNull AlertRecommendationDto recommendationDto);
}
