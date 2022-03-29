package com.silenteight.connector.ftcc.callback.outgoing;

public interface RecommendationsDeliveredPublisher {

  void publish(RecommendationsDeliveredEvent event);

}
