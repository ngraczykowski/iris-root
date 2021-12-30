package com.silenteight.bridge.core.recommendation.domain.port.outgoing;

import com.silenteight.bridge.core.recommendation.domain.model.RecommendationsReceivedEvent;

public interface RecommendationEventPublisher {

  void publish(RecommendationsReceivedEvent recommendationsReceivedEvent);
}
