package com.silenteight.qco.domain;

import lombok.experimental.UtilityClass;

import com.silenteight.qco.domain.model.QcoRecommendationAlert;
import com.silenteight.qco.domain.model.QcoRecommendationAlert.QcoMatchData;
import com.silenteight.qco.domain.model.QcoRecommendationMatch;

@UtilityClass
class QcoAlertMapper {

  QcoRecommendationMatch toQcoRecommendationMatch(
      QcoMatchData match, QcoRecommendationAlert alert) {
    return QcoRecommendationMatch.builder()
        .batchId(alert.batchId())
        .alertId(alert.alertId())
        .alertName(alert.alertName())
        .policyId(alert.policyId())
        .matchName(match.name())
        .stepId(match.stepId())
        .solution(match.recommendation())
        .comment(match.comment())
        .build();
  }
}
