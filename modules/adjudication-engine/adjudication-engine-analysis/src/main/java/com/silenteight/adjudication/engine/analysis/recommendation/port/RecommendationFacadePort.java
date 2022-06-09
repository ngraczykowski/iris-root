/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.adjudication.engine.analysis.recommendation.port;

import com.silenteight.adjudication.api.v1.RecommendationsGenerated.RecommendationInfo;
import com.silenteight.adjudication.engine.analysis.recommendation.domain.SaveRecommendationRequest;

import java.util.List;

public interface RecommendationFacadePort {

  List<RecommendationInfo> createRecommendations(SaveRecommendationRequest request);
}
