package com.silenteight.scb.outputrecommendation.domain.port.outgoing;

import com.silenteight.scb.outputrecommendation.domain.model.RecommendationsDeliveredEvent;

public interface RecommendationDeliveredEventPublisher {

  void publish(RecommendationsDeliveredEvent event);
}
