package com.silenteight.adjudication.engine.analysis.pendingrecommendation.integration;

import com.silenteight.adjudication.internal.v1.PendingRecommendations;

interface PendingRecommendationGateway {

  void send(PendingRecommendations pendingRecommendations);
}
