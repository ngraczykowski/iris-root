package com.silenteight.payments.bridge.svb.learning.features.service;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.location.v1.LocationFeatureInput;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.createFeatureInput;
import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.getFullFeatureName;

@Service
@Qualifier("geo")
class GeoFeatureExtractor implements FeatureExtractor {

  private static final String GEO_FEATURE = "geo";

  @Override
  public List<FeatureInput> createFeatureInputs(LearningMatch learningMatch) {
    var locationFeatureInput = createLocationFeatureInput(learningMatch);
    var featureInput = createFeatureInput(GEO_FEATURE, locationFeatureInput);
    return List.of(featureInput);
  }

  private static LocationFeatureInput createLocationFeatureInput(LearningMatch learningMatch) {
    return LocationFeatureInput
        .newBuilder()
        .setFeature(getFullFeatureName(GEO_FEATURE))
        .setWatchlistLocation(learningMatch.getWatchlistLocation())
        .setAlertedPartyLocation(learningMatch.getAlertedPartyLocation())
        .build();
  }
}
