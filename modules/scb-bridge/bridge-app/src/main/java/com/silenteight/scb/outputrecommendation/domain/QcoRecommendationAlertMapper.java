package com.silenteight.scb.outputrecommendation.domain;

import lombok.experimental.UtilityClass;

import com.silenteight.qco.domain.model.QcoRecommendationAlert;
import com.silenteight.qco.domain.model.QcoRecommendationAlert.QcoMatchData;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Match;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Recommendation;

import java.util.List;

@UtilityClass
class QcoRecommendationAlertMapper {

  QcoRecommendationAlert map(Recommendation recommendation) {
    return QcoRecommendationAlert.builder()
        .batchId(recommendation.batchId())
        .alertId(recommendation.alert().id())
        .alertName(recommendation.alert().name())
        .policyId(recommendation.policyId())
        .matches(toQcoMatchesData(recommendation))
        .build();
  }

  private List<QcoMatchData> toQcoMatchesData(Recommendation recommendation) {
    return recommendation.matches().stream()
        .map(QcoRecommendationAlertMapper::toQcoMatchData)
        .toList();
  }

  private QcoMatchData toQcoMatchData(Match match) {
    return QcoMatchData.builder()
        .name(match.name())
        .stepId(match.stepId())
        .comment(match.recommendedComment())
        .recommendation(match.recommendedAction())
        .build();
  }
}
