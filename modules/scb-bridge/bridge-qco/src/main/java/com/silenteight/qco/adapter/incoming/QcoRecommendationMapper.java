package com.silenteight.qco.adapter.incoming;

import lombok.experimental.UtilityClass;

import com.silenteight.proto.qco.api.v1.QcoMatch;
import com.silenteight.proto.qco.api.v1.QcoRecommendation;
import com.silenteight.qco.domain.model.QcoRecommendationAlert;
import com.silenteight.qco.domain.model.QcoRecommendationAlert.QcoMatchData;

import java.util.List;

@UtilityClass
class QcoRecommendationMapper {

  QcoRecommendationAlert toQcoRecommendationAlert(QcoRecommendation recommendation) {
    return QcoRecommendationAlert.builder()
        .batchId(recommendation.getBatchId())
        .alertId(recommendation.getAlertId())
        .policyId(recommendation.getPolicyId())
        .alertName(recommendation.getAlertName())
        .matches(toQcoMatchesData(recommendation.getMatchesList()))
        .onlyMark(recommendation.getOnlyMark())
        .build();
  }

  QcoRecommendation toQcoRecommendation(QcoRecommendationAlert recommendation) {
    return QcoRecommendation.newBuilder()
        .setBatchId(recommendation.batchId())
        .setAlertId(recommendation.alertId())
        .setAlertName(recommendation.alertName())
        .setPolicyId(recommendation.policyId())
        .addAllMatches(toQcoMatches(recommendation.matches()))
        .setOnlyMark(recommendation.onlyMark())
        .build();
  }

  private static List<QcoMatch> toQcoMatches(List<QcoMatchData> matches) {
    return matches.stream()
        .map(match -> QcoMatch.newBuilder()
            .setMatchId(match.id())
            .setMatchName(match.name())
            .setRecommendation(match.recommendation())
            .setComment(match.comment())
            .setFvSignature(match.fvSignature())
            .setStepId(match.stepId())
            .setQcoMarked(match.qcoMarked())
            .build())
        .toList();
  }

  private static List<QcoMatchData> toQcoMatchesData(List<QcoMatch> matches) {
    return matches.stream()
        .map(match -> QcoMatchData.builder()
            .id(match.getMatchId())
            .name(match.getMatchName())
            .recommendation(match.getRecommendation())
            .comment(match.getComment())
            .fvSignature(match.getFvSignature())
            .stepId(match.getStepId())
            .qcoMarked(match.getQcoMarked())
            .build())
        .toList();
  }
}
