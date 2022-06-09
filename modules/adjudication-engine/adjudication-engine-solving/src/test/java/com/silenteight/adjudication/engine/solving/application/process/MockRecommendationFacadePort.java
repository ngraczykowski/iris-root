/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.solving.application.process;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.api.v2.RecommendationMetadata;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.SaveRecommendationRequest;
import com.silenteight.adjudication.engine.analysis.recommendation.port.RecommendationFacadePort;

import java.util.List;

class MockRecommendationFacadePort implements RecommendationFacadePort {

  @Override
  public List<RecommendationInfo> createRecommendations(SaveRecommendationRequest request) {
    var alert = request.getAlertSolutions().get(0);
    return List.of(
        RecommendationInfo.newBuilder()
            .setRecommendation("recommendations/123")
            .setAlert("alerts/" + alert.getAlertId())
            .setRecommendation(alert.getRecommendedAction())
            .setMetadata(RecommendationMetadata.newBuilder().build())
            .build());
  }
}
