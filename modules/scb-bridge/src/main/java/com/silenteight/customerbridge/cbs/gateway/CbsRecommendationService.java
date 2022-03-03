package com.silenteight.customerbridge.cbs.gateway;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.customerbridge.common.protocol.AlertWrapper;
import com.silenteight.proto.serp.scb.v1.ScbAlertDetails;
import com.silenteight.proto.serp.v1.alert.Alert;
import com.silenteight.proto.serp.v1.recommendation.AlertRecommendation;
import com.silenteight.proto.serp.v1.recommendation.Recommendation;
import com.silenteight.proto.serp.v1.recommendation.RecommendedAction;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@RequiredArgsConstructor
@Slf4j
public class CbsRecommendationService {

  private final CbsRecommendationGateway cbsRecommendationGateway;
  private final Map<String, String> recommendationValues;

  public void recommend(List<AlertRecommendation> alertRecommendations) {
    var alertsToBeRecommended = getAlertsToBeRecommended(alertRecommendations);

    cbsRecommendationGateway.recommendAlerts(alertsToBeRecommended);
  }

  private List<CbsAlertRecommendation> getAlertsToBeRecommended(List<AlertRecommendation> alerts) {
    return alerts.stream()
        .filter(r -> isWatchlistLevelProcessing(r.getAlert()))
        .map(this::mapAlertRecommendation)
        .collect(Collectors.toList());
  }

  private CbsAlertRecommendation mapAlertRecommendation(AlertRecommendation alertRecommendation) {
    AlertWrapper alertWrapper = new AlertWrapper(alertRecommendation.getAlert());
    ScbAlertDetails scbAlertDetails = unpackAlertDetails(alertWrapper);
    Recommendation recommendation = alertRecommendation.getRecommendation();

    return CbsAlertRecommendation.builder()
        .alertExternalId(scbAlertDetails.getSystemId())
        .batchId(scbAlertDetails.getBatchId())
        .hitWatchlistId(scbAlertDetails.getWatchlistId())
        .hitRecommendedStatus(getRecommendationValue(recommendation.getAction()))
        .hitRecommendedComments(recommendation.getComment())
        .build();
  }

  private String getRecommendationValue(RecommendedAction action) {
    return Optional.ofNullable(recommendationValues.get(action.name())).orElseGet(() -> {
      log.warn("User-friendly value not configured for RecommendedAction: {}", action);
      return action.name();
    });
  }

  private static ScbAlertDetails unpackAlertDetails(AlertWrapper accessor) {
    return accessor
        .unpackDetails(ScbAlertDetails.class)
        .orElseThrow(() ->
            new IllegalArgumentException("Alert " + accessor.getSourceId() + " has no details"));
  }

  private static boolean isWatchlistLevelProcessing(Alert alert) {
    AlertWrapper alertWrapper = new AlertWrapper(alert);
    ScbAlertDetails details = alertWrapper.unpackDetails(ScbAlertDetails.class)
        .orElseThrow(() -> new IllegalArgumentException("Alert has no details"));
    return isNotEmpty(details.getWatchlistId());
  }
}
