/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.domain;

import lombok.experimental.UtilityClass;

import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.Match;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.Recommendation;
import com.silenteight.iris.qco.domain.model.QcoRecommendationAlert;

import java.util.List;

@UtilityClass
class MatchOverrider {

  Recommendation overrideMatches(
      Recommendation recommendation, QcoRecommendationAlert qcoRecommendationAlert) {
    return recommendation.toBuilder()
        .matches(toMatchList(qcoRecommendationAlert))
        .build();
  }

  private List<Match> toMatchList(QcoRecommendationAlert updatedRecommendations) {
    return updatedRecommendations.matches().stream()
        .map(QcoRecommendationAlertMapper::toMatch)
        .toList();
  }
}
