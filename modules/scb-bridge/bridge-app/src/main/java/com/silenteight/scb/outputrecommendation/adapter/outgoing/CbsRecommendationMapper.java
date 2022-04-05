package com.silenteight.scb.outputrecommendation.adapter.outgoing;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.scb.ingest.domain.model.AlertMetadata;
import com.silenteight.scb.ingest.domain.payload.PayloadConverter;
import com.silenteight.scb.outputrecommendation.domain.model.CbsAlertRecommendation;
import com.silenteight.scb.outputrecommendation.domain.model.CbsAlertRecommendation.CbsAlertRecommendationBuilder;
import com.silenteight.scb.outputrecommendation.domain.model.CbsAlertRecommendation.QcoInfo;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Alert;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Match;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.Recommendation;
import com.silenteight.scb.outputrecommendation.domain.model.Recommendations.RecommendedAction;
import com.silenteight.scb.outputrecommendation.infrastructure.CbsRecommendationMapperConfigurationProperties;
import com.silenteight.scb.outputrecommendation.infrastructure.CbsRecommendationProperties;

import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Slf4j
@RequiredArgsConstructor
public class CbsRecommendationMapper {

  private final PayloadConverter payloadConverter;
  private final CbsRecommendationProperties cbsRecommendationProperties;
  private final CbsRecommendationMapperConfigurationProperties mapperConfigurationProperties;

  public List<CbsAlertRecommendation> getAlertsToBeRecommended(
      List<Recommendations.Recommendation> recommendations) {
    return recommendations.stream()
        .filter(recommendation -> isWatchlistLevelProcessing(recommendation.alert()))
        .map(this::mapAlertRecommendation)
        .toList();
  }

  private boolean isWatchlistLevelProcessing(Alert alert) {
    var alertMetadata = parseAlertMetadata(alert);
    return isNotEmpty(alertMetadata.watchlistId());
  }

  private AlertMetadata parseAlertMetadata(Alert alert) {
    return payloadConverter.deserializeFromJsonToObject(alert.metadata(), AlertMetadata.class);
  }

  private CbsAlertRecommendation mapAlertRecommendation(Recommendation recommendation) {
    AlertMetadata alertMetadata = parseAlertMetadata(recommendation.alert());
    CbsAlertRecommendationBuilder builder = CbsAlertRecommendation.builder()
        .alertExternalId(alertMetadata.systemId())
        .batchId(recommendation.batchId())
        .hitWatchlistId(alertMetadata.watchlistId())
        .hitRecommendedStatus(getRecommendationValue(recommendation.recommendedAction()))
        .hitRecommendedComments(recommendation.recommendedComment());

    if (mapperConfigurationProperties.attachQcoFieldsToRecom()) {
      builder.qcoInfo(getQcoInfo(recommendation));
    }

    return builder.build();
  }

  private String getRecommendationValue(RecommendedAction action) {
    var recommendationValue =
        cbsRecommendationProperties.getRecommendationValues().get(action.name());
    if (recommendationValue == null) {
      log.warn("User-friendly value not configured for RecommendedAction: {}", action);
      return action.name();
    }
    return recommendationValue;
  }

  private QcoInfo getQcoInfo(Recommendation recommendation) {
    List<Match> matches = recommendation.matches();
    if (CollectionUtils.isEmpty(matches) || matches.size() != 1) {
      throw new IllegalStateException(
          String.format(
              "Alert with id %s should have only one match on watchlist-level processing",
              recommendation.alert().id()));
    }
    Match match = recommendation.matches().get(0);
    return QcoInfo.builder()
        .policyId(recommendation.policyId())
        .hitId(match.id())
        .stepId(match.stepId())
        .fvSignature(match.fvSignature())
        .build();
  }
}
