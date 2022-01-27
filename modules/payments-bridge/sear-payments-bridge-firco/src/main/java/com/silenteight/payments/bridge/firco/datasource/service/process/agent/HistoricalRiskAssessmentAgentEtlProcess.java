package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.historicaldecisions.v2.HistoricalDecisionsFeatureInput;
import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentFeatureRequest;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentFeatureUseCase;
import com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Component
@RequiredArgsConstructor
class HistoricalRiskAssessmentAgentEtlProcess
    extends BaseAgentEtlProcess<HistoricalDecisionsFeatureInput> {

  private final List<HistoricalRiskAssessmentFeature> features;
  private final HistoricalRiskAssessmentFeatureUseCase historicalRiskAssessmentFeatureAgent;

  @Override
  protected List<FeatureInput> createDataSourceFeatureInputs(HitData hitData) {
    return features.stream()
        .map(feature -> createFeatureInput(hitData, feature))
        .collect(toList());
  }

  private FeatureInput createFeatureInput(
      HitData hitData, HistoricalRiskAssessmentFeature feature) {

    var historicalDecisionsFeatureInput =
        getHistoricalDecisionsFeatureInput(hitData, feature);

    return AgentDataSourceUtils.createFeatureInput(
        feature.getFeatureName(), historicalDecisionsFeatureInput);
  }

  private HistoricalDecisionsFeatureInput getHistoricalDecisionsFeatureInput(
      HitData hitData, HistoricalRiskAssessmentFeature feature) {

    var request =
        createHistoricalRiskAgentRequest(hitData, feature);
    return historicalRiskAssessmentFeatureAgent.invoke(request);
  }

  private static HistoricalRiskAssessmentAgentFeatureRequest createHistoricalRiskAgentRequest(
      HitData hitData, HistoricalRiskAssessmentFeature feature) {

    var featureName = feature.getFeatureName();
    var matchId = hitData.getMatchId();
    var alertedPartyId = feature.getAlertedPartyId(hitData.getAlertedPartyData());
    var ofacId = hitData.getHitAndWlPartyData().getId().toUpperCase().trim();
    var watchlistType = hitData.getHitAndWlPartyData().getWatchlistType().toString();

    return HistoricalRiskAssessmentAgentFeatureRequest.builder()
        .feature(featureName)
        .matchId(matchId)
        .alertedPartyId(alertedPartyId)
        .ofacId(ofacId)
        .watchlistType(watchlistType)
        .build();
  }
}
