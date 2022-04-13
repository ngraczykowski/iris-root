package com.silenteight.scb.outputrecommendation.domain;

import lombok.experimental.UtilityClass;

import com.silenteight.qco.domain.model.QcoRecommendationAlert;
import com.silenteight.qco.domain.model.QcoRecommendationAlert.QcoMatchData;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Match;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
class MatchOverrider {

  List<Match> override(List<Match> matches, QcoRecommendationAlert updatedRecommendations) {
    var updatedMatches = new ArrayList<Match>();
    matches.forEach(match ->
        updatedMatches.add(
            overrideRecommendation(
                match, findQcoMatchData(match.name(), updatedRecommendations.matches()))));
    return updatedMatches;
  }

  private QcoMatchData findQcoMatchData(String name, List<QcoMatchData> matches) {
    return matches.stream()
        .filter(qcoMatchData -> qcoMatchData.name().equals(name))
        .findFirst()
        .orElseThrow();
  }

  private Match overrideRecommendation(Match match, QcoMatchData qcoMatchData) {
    return match.toBuilder()
        .recommendedAction(qcoMatchData.recommendation())
        .recommendedComment(qcoMatchData.comment())
        .build();
  }
}
