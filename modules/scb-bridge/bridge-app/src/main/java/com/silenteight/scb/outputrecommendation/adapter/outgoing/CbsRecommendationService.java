package com.silenteight.scb.outputrecommendation.adapter.outgoing;

import lombok.RequiredArgsConstructor;

import com.silenteight.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations;

import java.util.List;

@RequiredArgsConstructor
public class CbsRecommendationService {

  private final CbsRecommendationGateway cbsRecommendationGateway;
  private final CbsRecommendationMapper cbsRecommendationMapper;
  private final AlertInFlightService alertInFlightService;

  public void recommend(List<Recommendations.Recommendation> recommendations) {
    var alertsToBeRecommended = cbsRecommendationMapper.getAlertsToBeRecommended(recommendations);
    cbsRecommendationGateway.recommendAlerts(alertsToBeRecommended);
    alertInFlightService.deleteAlerts(
        AlertIdMapper.mapFromAlertRecommendations(alertsToBeRecommended));
  }
}
