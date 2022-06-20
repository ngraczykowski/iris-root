/*
 * Copyright (c) 2022 Silent Eight Pte. Ltd. All rights reserved.
 */

package com.silenteight.iris.bridge.scb.outputrecommendation.adapter.outgoing;

import lombok.RequiredArgsConstructor;

import com.silenteight.iris.bridge.scb.ingest.adapter.incomming.cbs.alertunderprocessing.AlertInFlightService;
import com.silenteight.iris.bridge.scb.outputrecommendation.domain.model.Recommendations.Recommendation;

import java.util.List;

@RequiredArgsConstructor
public class CbsRecommendationService {

  private final CbsRecommendationGateway cbsRecommendationGateway;
  private final CbsRecommendationMapper cbsRecommendationMapper;
  private final AlertInFlightService alertInFlightService;

  public void recommend(List<Recommendation> recommendations) {
    var alertsToBeRecommended = cbsRecommendationMapper.getAlertsToBeRecommended(recommendations);
    cbsRecommendationGateway.recommendAlerts(alertsToBeRecommended);
    alertInFlightService.deleteAlerts(
        AlertIdMapper.mapFromAlertRecommendations(alertsToBeRecommended));
  }
}
