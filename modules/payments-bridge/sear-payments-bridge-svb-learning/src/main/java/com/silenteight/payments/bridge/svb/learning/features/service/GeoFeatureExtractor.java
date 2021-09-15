package com.silenteight.payments.bridge.svb.learning.features.service;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.location.v1.LocationFeatureInput;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningMatch;

import com.google.protobuf.Any;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("geo")
class GeoFeatureExtractor implements FeatureExtractor {

  @Override
  public FeatureInput extract(LearningMatch learningMatch) {
    return FeatureInput
        .newBuilder()
        .setFeature("features/geo")
        .setAgentFeatureInput(Any.pack(LocationFeatureInput
            .newBuilder()
            .setFeature("features/geo")
            .setWatchlistLocation(learningMatch.getWatchlistLocation())
            .setAlertedPartyLocation(learningMatch.getAlertedPartyLocation())
            .build()))
        .build();
  }
}
