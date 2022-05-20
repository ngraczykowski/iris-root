package com.silenteight.payments.bridge.firco.recommendation.port;

import com.silenteight.payments.bridge.firco.recommendation.model.AdjudicationEngineSourcedRecommendation;
import com.silenteight.payments.bridge.firco.recommendation.model.BridgeSourcedRecommendation;

public interface CreateRecommendationUseCase {

  void create(BridgeSourcedRecommendation source);

  void create(AdjudicationEngineSourcedRecommendation source);

}
