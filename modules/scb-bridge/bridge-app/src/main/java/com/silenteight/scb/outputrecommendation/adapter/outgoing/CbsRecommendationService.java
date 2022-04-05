package com.silenteight.scb.outputrecommendation.adapter.outgoing;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.outputrecommendation.domain.model.Recommendations;

import java.util.List;

@RequiredArgsConstructor
public class CbsRecommendationService {

  private final CbsRecommendationGateway cbsRecommendationGateway;
  private final CbsRecommendationMapper cbsRecommendationMapper;

  public void recommend(List<Recommendations.Recommendation> recommendations) {
    var alertsToBeRecommended = cbsRecommendationMapper.getAlertsToBeRecommended(recommendations);
    cbsRecommendationGateway.recommendAlerts(alertsToBeRecommended);
  }
}
