package com.silenteight.payments.bridge.agents.service;

import com.silenteight.datasource.api.location.v1.LocationFeatureInput;
import com.silenteight.payments.bridge.agents.model.GeoAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateLocationFeatureInputUseCase;

import org.springframework.stereotype.Component;

@Component
class CreateLocationFeatureInput implements CreateLocationFeatureInputUseCase {

  @Override
  public LocationFeatureInput create(GeoAgentRequest request) {

    return LocationFeatureInput.newBuilder()
        .setFeature(request.getFeature())
        .setAlertedPartyLocation(request.getAlertedPartyLocation())
        .setWatchlistLocation(request.getWatchlistLocation())
        .build();
  }
}
