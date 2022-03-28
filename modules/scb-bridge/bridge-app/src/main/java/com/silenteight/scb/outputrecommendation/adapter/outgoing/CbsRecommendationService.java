package com.silenteight.scb.outputrecommendation.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.domain.model.AlertMetadata;
import com.silenteight.scb.ingest.domain.payload.PayloadConverter;
import com.silenteight.scb.outputrecommendation.domain.model.CbsAlertRecommendation;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Alert;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Recommendation;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.RecommendedAction;

import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@RequiredArgsConstructor
@Slf4j
public class CbsRecommendationService {

  private final CbsRecommendationGateway cbsRecommendationGateway;
  private final Map<String, String> recommendationValues;
  private final PayloadConverter payloadConverter;

  public void recommend(List<Recommendations.Recommendation> recommendations) {
    var alertsToBeRecommended = getAlertsToBeRecommended(recommendations);
    cbsRecommendationGateway.recommendAlerts(alertsToBeRecommended);
  }

  private List<CbsAlertRecommendation> getAlertsToBeRecommended(
      List<Recommendations.Recommendation> recommendations) {
    return recommendations.stream()
        .filter(recommendation -> isWatchlistLevelProcessing(recommendation.alert()))
        .map(this::mapAlertRecommendation)
        .toList();
  }

  private CbsAlertRecommendation mapAlertRecommendation(Recommendation recommendation) {
    AlertMetadata alertMetadata = parseAlertMetadata(recommendation.alert());
    return CbsAlertRecommendation.builder()
        .alertExternalId(recommendation.alert().id())
        .batchId(recommendation.batchId())
        .hitWatchlistId(alertMetadata.watchlistId())
        .hitRecommendedStatus(getRecommendationValue(recommendation.recommendedAction()))
        .hitRecommendedComments(recommendation.recommendedComment())
        .build();
  }

  private String getRecommendationValue(RecommendedAction action) {
    var recommendationValue = recommendationValues.get(action.name());
    if (recommendationValue == null) {
      log.warn("User-friendly value not configured for RecommendedAction: {}", action);
      return action.name();
    }
    return recommendationValue;
  }

  private boolean isWatchlistLevelProcessing(Alert alert) {
    var alertMetadata = parseAlertMetadata(alert);
    return isNotEmpty(alertMetadata.watchlistId());
  }

  private AlertMetadata parseAlertMetadata(Alert alert) {
    return payloadConverter.deserializeFromJsonToObject(alert.metadata(), AlertMetadata.class);
  }
}
