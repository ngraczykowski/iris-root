package com.silenteight.bridge.core.recommendation.domain.port.outgoing;

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsStoredEvent;

public interface RecommendationEventPublisher {

  void publish(RecommendationsStoredEvent recommendationsStoredEvent);
}
