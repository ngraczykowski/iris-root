package com.silenteight.qco.adapter.outgoing.jpa;


import lombok.NoArgsConstructor;

import com.silenteight.qco.domain.model.QcoRecommendationMatch;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class QcoOverriddenRecommendationMapper {

  public static QcoOverriddenRecommendation toQcoOverriddenRecommendation(
      QcoRecommendationMatch match, String targetSolution) {
    return QcoOverriddenRecommendation.builder()
        .batchId(match.batchId())
        .alertId(match.alertId())
        .alertName(match.alertName())
        .comment(match.comment())
        .matchName(match.matchName())
        .policyId(match.policyId())
        .stepId(match.stepId())
        .sourceSolution(match.solution())
        .targetSolution(targetSolution)
        .build();
  }
}
