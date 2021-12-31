package com.silenteight.payments.bridge.agents.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.historicaldecisions.v2.*;
import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentFeatureRequest;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentFeatureUseCase;

import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
//NOTE(jgajewski): Remove 'Feature' from class name 'HistoricalRiskAssessmentFeatureAgent',
// when category use case is going to be deleted, after successful testing
class HistoricalRiskAssessmentFeatureAgent implements HistoricalRiskAssessmentFeatureUseCase {

  private static final String FEATURE_PREFIX = "features/";

  @Override
  public HistoricalDecisionsFeatureInput invoke(
      HistoricalRiskAssessmentAgentFeatureRequest request) {

    var featureName = getFeatureName(request.getFeature());

    return HistoricalDecisionsFeatureInput.newBuilder()
        .setFeature(featureName)
        .setModelKey(createModelKey(request))
        .setDiscriminator(createDiscriminator(request.getMatchId()))
        .build();
  }

  private static ModelKey createModelKey(HistoricalRiskAssessmentAgentFeatureRequest request) {
    return ModelKey.newBuilder()
        .setMatch(createMatch(request))
        .build();
  }

  private static Match createMatch(HistoricalRiskAssessmentAgentFeatureRequest request) {

    var alertedPartyId = request.getAlertedPartyId();
    var alertedParty = createAlertedParty(alertedPartyId);

    var watchlistParty = createWatchlistParty(request);

    return Match.newBuilder()
        .setAlertedParty(alertedParty)
        .setWatchlistParty(watchlistParty)
        .build();
  }

  private static AlertedParty createAlertedParty(String alertedPartyId) {
    return AlertedParty.newBuilder()
        .setId(alertedPartyId)
        .build();
  }

  private static WatchlistParty createWatchlistParty(
      HistoricalRiskAssessmentAgentFeatureRequest request) {

    var ofacId = request.getOfacId();
    var watchlistType = request.getWatchlistType();

    return WatchlistParty.newBuilder()
        .setId(ofacId)
        .setType(watchlistType)
        .build();
  }

  private static Discriminator createDiscriminator(String matchId) {
    return Discriminator.newBuilder()
        .setValue(matchId)
        .build();
  }

  private static String getFeatureName(String featureName) {
    return FEATURE_PREFIX + featureName;
  }
}
