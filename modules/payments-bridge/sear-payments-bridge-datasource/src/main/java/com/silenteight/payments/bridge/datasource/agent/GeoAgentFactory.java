package com.silenteight.payments.bridge.datasource.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.location.v1.LocationFeatureInput;
import com.silenteight.payments.bridge.agents.model.GeoAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateLocationFeatureInputUseCase;
import com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured.GeoAgentData;

import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.GEO_FEATURE;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.GEO_FEATURE_NAME;

@Component
@RequiredArgsConstructor
class GeoAgentFactory extends BaseFeatureInputStructuredFactory {

  private final CreateLocationFeatureInputUseCase createLocationFeatureInputUseCase;

  @Override
  protected FeatureInput createFeatureInput(FeatureInputStructured featureInputStructured) {
    var geoAgentData = featureInputStructured.getGeoAgentData();
    var locationFeatureInput = getLocationFeatureInput(geoAgentData);
    return AgentDataSourceUtils.createFeatureInput(GEO_FEATURE_NAME, locationFeatureInput);
  }

  @Override
  protected String getFeatureName() {
    return GEO_FEATURE_NAME;
  }

  private LocationFeatureInput getLocationFeatureInput(GeoAgentData geoAgentData) {
    var geoAgentRequest = createGeoAgentRequest(geoAgentData);
    return createLocationFeatureInputUseCase.create(geoAgentRequest);
  }

  private static GeoAgentRequest createGeoAgentRequest(GeoAgentData geoAgentData) {
    return GeoAgentRequest.builder()
        .feature(GEO_FEATURE)
        .alertedPartyLocation(geoAgentData.getAlertedPartyLocation())
        .watchlistLocation(geoAgentData.getWatchListLocation())
        .build();
  }
}
