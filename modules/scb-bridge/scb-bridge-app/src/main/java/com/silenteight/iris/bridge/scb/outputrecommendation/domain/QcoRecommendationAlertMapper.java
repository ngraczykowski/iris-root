/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.domain;

import lombok.experimental.UtilityClass;

import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.Match;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.Recommendation;
import com.silenteight.iris.qco.domain.model.QcoRecommendationAlert;
import com.silenteight.iris.qco.domain.model.QcoRecommendationAlert.QcoMatchData;

import java.util.List;

import static org.apache.commons.lang3.BooleanUtils.toStringYesNo;

@UtilityClass
class QcoRecommendationAlertMapper {

  QcoRecommendationAlert map(Recommendation recommendation, boolean onlyMark) {
    return QcoRecommendationAlert.builder()
        .batchId(recommendation.batchId())
        .alertId(recommendation.alert().id())
        .alertName(recommendation.alert().name())
        .policyId(recommendation.policyId())
        .matches(toQcoMatchesData(recommendation))
        .onlyMark(onlyMark)
        .build();
  }

  Match toMatch(QcoMatchData qcoMatchData) {
    return Match.builder()
        .id(qcoMatchData.id())
        .name(qcoMatchData.name())
        .stepId(qcoMatchData.stepId())
        .recommendedAction(qcoMatchData.recommendation())
        .recommendedComment(qcoMatchData.comment())
        .fvSignature(qcoMatchData.fvSignature())
        .qaSampled(toStringYesNo(qcoMatchData.qcoMarked()))
        .build();
  }

  private List<QcoMatchData> toQcoMatchesData(Recommendation recommendation) {
    return recommendation.matches().stream()
        .map(QcoRecommendationAlertMapper::toQcoMatchData)
        .toList();
  }

  private QcoMatchData toQcoMatchData(Match match) {
    return QcoMatchData.builder()
        .id(match.id())
        .name(match.name())
        .stepId(match.stepId())
        .comment(match.recommendedComment())
        .recommendation(match.recommendedAction())
        .fvSignature(match.fvSignature())
        .build();
  }
}
