/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.qco.domain;

import lombok.experimental.UtilityClass;

import com.silenteight.iris.qco.domain.model.QcoRecommendationAlert;
import com.silenteight.iris.qco.domain.model.QcoRecommendationAlert.QcoMatchData;
import com.silenteight.iris.qco.domain.model.QcoRecommendationMatch;

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
        .onlyMark(alert.onlyMark())
        .build();
  }
}
