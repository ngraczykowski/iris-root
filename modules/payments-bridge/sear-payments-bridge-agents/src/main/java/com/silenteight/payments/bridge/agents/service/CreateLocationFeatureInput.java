package com.silenteight.payments.bridge.agents.service;

import com.silenteight.datasource.api.location.v1.LocationFeatureInput;
import com.silenteight.payments.bridge.agents.model.GeoAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateLocationFeatureInputUseCase;

import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.FEATURE_PREFIX;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.GEO_FEATURE;

@Component
class CreateLocationFeatureInput implements CreateLocationFeatureInputUseCase {

  @Override
  public LocationFeatureInput create(GeoAgentRequest request) {

    return LocationFeatureInput.newBuilder()
        .setFeature(getFeatureName(GEO_FEATURE))
        .setAlertedPartyLocation(request.getAlertedPartyLocation())
        .setWatchlistLocation(request.getWatchlistLocation())
        .build();
  }

  private static String getFeatureName(String featureName) {
    return FEATURE_PREFIX + featureName;
  }

}
