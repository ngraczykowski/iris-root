package com.silenteight.scb.outputrecommendation.domain;

import lombok.experimental.UtilityClass;

import com.silenteight.qco.domain.model.QcoRecommendationAlert;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Match;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Recommendation;

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
