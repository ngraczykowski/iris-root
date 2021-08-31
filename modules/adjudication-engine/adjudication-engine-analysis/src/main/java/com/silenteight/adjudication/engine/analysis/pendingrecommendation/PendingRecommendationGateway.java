package com.silenteight.adjudication.engine.analysis.pendingrecommendation;

import com.silenteight.adjudication.internal.v1.PendingRecommendations;

public interface PendingRecommendationGateway {

  void send(PendingRecommendations pendingRecommendations);
}
