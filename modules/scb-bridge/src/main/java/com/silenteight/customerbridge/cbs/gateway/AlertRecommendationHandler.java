package com.silenteight.customerbridge.cbs.gateway;

import lombok.RequiredArgsConstructor;

import com.silenteight.customerbridge.common.recommendation.ScbRecommendationService;
import com.silenteight.proto.serp.v1.recommendation.AlertRecommendation;

import org.springframework.messaging.MessageHeaders;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class AlertRecommendationHandler {

  private final CbsRecommendationService cbsRecommendationService;
  private final ScbRecommendationService scbRecommendationService;

  public List<AlertRecommendation> handle(
      List<AlertRecommendation> payload, MessageHeaders headers) {

    var alertRecommendations = filterHavingAlerts(payload);

    cbsRecommendationService.recommend(alertRecommendations);
    scbRecommendationService.saveRecommendations(alertRecommendations);

    return null;
  }

  private List<AlertRecommendation> filterHavingAlerts(
      List<AlertRecommendation> alertRecommendations) {
    return alertRecommendations.stream()
        .filter(AlertRecommendation::hasAlert)
        .collect(Collectors.toList());
  }
}
