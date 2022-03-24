package com.silenteight.scb.outputrecommendation.domain.port.outgoing;

import com.silenteight.scb.outputrecommendation.domain.model.ErrorRecommendationsGeneratedEvent;
import com.silenteight.scb.outputrecommendation.domain.model.RecommendationsGeneratedEvent;

public interface RecommendationPublisher {

  void publishCompleted(RecommendationsGeneratedEvent event);

  void publishError(ErrorRecommendationsGeneratedEvent event);
}
