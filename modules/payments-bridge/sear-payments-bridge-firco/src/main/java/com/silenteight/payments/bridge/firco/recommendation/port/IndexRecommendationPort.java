package com.silenteight.payments.bridge.firco.recommendation.port;

import com.silenteight.payments.bridge.firco.recommendation.model.AdjudicationEngineSourcedRecommendation;
import com.silenteight.payments.bridge.firco.recommendation.model.BridgeSourcedRecommendation;

public interface IndexRecommendationPort {

  void send(AdjudicationEngineSourcedRecommendation event);

  void send(BridgeSourcedRecommendation bridgeSourcedRecommendation);

}
