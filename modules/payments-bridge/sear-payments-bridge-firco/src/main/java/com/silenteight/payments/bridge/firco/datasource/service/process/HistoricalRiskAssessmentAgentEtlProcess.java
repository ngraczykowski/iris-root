package com.silenteight.payments.bridge.firco.datasource.service.process;

import com.silenteight.datasource.agentinput.api.v1.AgentInputServiceGrpc.AgentInputServiceBlockingStub;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.historicaldecisions.v2.*;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import com.google.protobuf.Any;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

class HistoricalRiskAssessmentAgentEtlProcess
    extends BaseAgentEtlProcess<HistoricalDecisionsFeatureInput> {

  private final List<HistoricalRiskAssessmentFeature> features;

  HistoricalRiskAssessmentAgentEtlProcess(
      AgentInputServiceBlockingStub blockingStub, Duration timeout,
      List<HistoricalRiskAssessmentFeature> features) {
    super(blockingStub, timeout);
    this.features = features;
  }

  @Override
  protected List<FeatureInput> createDataSourceFeatureInputs(HitData hitData) {
    var featureInputs = new ArrayList<FeatureInput>();

    for (var feature : features) {
      var featureInput = getFeatureInput(hitData, feature);
      featureInputs.add(featureInput);
    }

    return featureInputs;
  }

  private static FeatureInput getFeatureInput(
      HitData hitData, HistoricalRiskAssessmentFeature feature) {
    var featureName = feature.getFeatureName();

    return FeatureInput
        .newBuilder()
        .setFeature(getFullFeatureName(featureName))
        .setAgentFeatureInput(Any.pack(getHistoricalFeatureInput(hitData, feature)))
        .build();
  }

  private static HistoricalDecisionsFeatureInput getHistoricalFeatureInput(
      HitData hitData, HistoricalRiskAssessmentFeature feature) {

    var featureName = feature.getFeatureName();
    var alertedPartyId = feature.getAlertedPartyId(hitData.getAlertedPartyData());

    return HistoricalDecisionsFeatureInput.newBuilder()
        .setFeature(getFullFeatureName(featureName))
        .setModelKey(getModelKey(hitData, alertedPartyId))
        .setDiscriminator(getDiscriminator(hitData))
        .build();
  }

  private static ModelKey getModelKey(HitData hitData, String alertedPartyId) {
    var match = createMatch(hitData, alertedPartyId);
    return ModelKey.newBuilder()
        .setMatch(match)
        .build();
  }

  private static Match createMatch(HitData hitData, String alertedPartyId) {

    var alertedParty = createAlertedParty(alertedPartyId);
    var watchlistParty = createWatchlistParty(hitData);

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

  private static WatchlistParty createWatchlistParty(HitData hitData) {

    var ofacId = hitData.getHitAndWlPartyData().getId().toUpperCase().trim();
    var watchlistType = hitData.getHitAndWlPartyData().getWatchlistType().toString();

    return WatchlistParty.newBuilder()
        .setId(ofacId)
        .setType(watchlistType)
        .build();
  }

  private static Discriminator getDiscriminator(
      HitData hitData) {
    var discriminatorMatchId = hitData.getMatchId();
    return Discriminator.newBuilder()
        .setValue(discriminatorMatchId)
        .build();
  }
}
