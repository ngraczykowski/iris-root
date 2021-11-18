package com.silenteight.payments.bridge.firco.alertmessage.port;

import com.silenteight.payments.bridge.event.RecommendationCompletedEvent;

public interface RecommendationCompletedPublisherPort {

  void send(RecommendationCompletedEvent event);

}
