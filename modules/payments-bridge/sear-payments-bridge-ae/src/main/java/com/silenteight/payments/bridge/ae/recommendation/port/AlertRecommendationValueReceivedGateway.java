package com.silenteight.payments.bridge.ae.recommendation.port;

import com.silenteight.adjudication.api.v1.Recommendation;

public interface AlertRecommendationValueReceivedGateway {

  void send(Recommendation recommendation);
}
