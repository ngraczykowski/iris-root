package com.silenteight.payments.bridge.svb.learning.step.etl.feature.service;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.location.v1.LocationFeatureInput;
import com.silenteight.payments.bridge.svb.learning.domain.EtlHit;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.GEO_FEATURE;
import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.createFeatureInput;
import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.getFullFeatureName;

@Service
@Qualifier("geo")
class GeoFeatureExtractorService implements FeatureExtractor {

  @Override
  public FeatureInput createFeatureInputs(EtlHit etlHit) {
    var locationFeatureInput = createLocationFeatureInput(etlHit);
    return createFeatureInput(GEO_FEATURE, locationFeatureInput);
  }

  private static LocationFeatureInput createLocationFeatureInput(EtlHit etlHit) {
    return LocationFeatureInput
        .newBuilder()
        .setFeature(getFullFeatureName(GEO_FEATURE))
        .setWatchlistLocation(etlHit.getWatchlistLocation())
        .setAlertedPartyLocation(etlHit.getAlertedPartyLocation())
        .build();
  }
}
