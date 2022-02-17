package com.silenteight.payments.bridge.agents.port;

import com.silenteight.datasource.api.location.v1.LocationFeatureInput;
import com.silenteight.payments.bridge.agents.model.GeoAgentRequest;

public interface CreateLocationFeatureInputUseCase {

  LocationFeatureInput create(GeoAgentRequest geoAgentRequest);
}
