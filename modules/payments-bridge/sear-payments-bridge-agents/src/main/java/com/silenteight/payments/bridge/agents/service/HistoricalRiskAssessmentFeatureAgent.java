package com.silenteight.payments.bridge.agents.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.datasource.api.historicaldecisions.v2.*;
import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentFeatureRequest;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentFeatureUseCase;
import com.silenteight.payments.bridge.common.app.LearningProperties;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableConfigurationProperties(LearningProperties.class)
//NOTE(jgajewski): Remove 'Feature' from class name 'HistoricalRiskAssessmentFeatureAgent',
// when category use case is going to be deleted, after successful testing
class HistoricalRiskAssessmentFeatureAgent implements HistoricalRiskAssessmentFeatureUseCase {

  private final LearningProperties properties;

  @Override
  public HistoricalDecisionsFeatureInput invoke(
      HistoricalRiskAssessmentAgentFeatureRequest request) {

    return HistoricalDecisionsFeatureInput.newBuilder()
        .setFeature(request.getFeature())
        .setModelKey(createModelKey(request))
        .setDiscriminator(createDiscriminator(request.getDiscriminator()))
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

  private Discriminator createDiscriminator(String discriminator) {
    return Discriminator.newBuilder()
        .setValue(getDiscriminatorValue(discriminator))
        .build();
  }

  private String getDiscriminatorValue(String discriminator) {
    return properties.getDiscriminatorPrefix() + "_" + discriminator;
  }
}
