package com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.service;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.historicaldecisions.v2.HistoricalDecisionsFeatureInput;
import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentFeatureRequest;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentFeatureUseCase;
import com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils;
import com.silenteight.payments.bridge.svb.newlearning.domain.EtlHit;
import com.silenteight.payments.bridge.svb.oldetl.response.AlertedPartyData;

abstract class HistoricalRiskAssessmentExtractorBase implements FeatureExtractor {

  abstract HistoricalRiskAssessmentFeatureUseCase getHistoricalRiskExtractor();

  protected abstract String getFeature();

  protected abstract String getDiscriminator();

  protected abstract String getAlertedPartyId(AlertedPartyData alertedPartyData);

  @Override
  public FeatureInput createFeatureInputs(EtlHit etlHit) {

    var historicalDecisionsFeatureInput =
        getHistoricalDecisionsFeatureInput(etlHit);

    return AgentDataSourceUtils.createFeatureInput(getFeature(), historicalDecisionsFeatureInput);
  }

  private HistoricalDecisionsFeatureInput getHistoricalDecisionsFeatureInput(
      EtlHit etlHit) {

    var request =
        createHistoricalRiskAgentRequest(etlHit);
    return getHistoricalRiskExtractor().invoke(request);
  }

  private HistoricalRiskAssessmentAgentFeatureRequest createHistoricalRiskAgentRequest(
      EtlHit etlHit) {

    var featureName = getFeature();
    var matchId = etlHit.getMatchId();
    var alertedPartyId = getAlertedPartyId(etlHit.getAlertedPartyData());
    var ofacId = etlHit.getFkcoVListFmmId();
    var watchlistType = etlHit.getWatchlistType().toString();

    return HistoricalRiskAssessmentAgentFeatureRequest.builder()
        .feature(featureName)
        .matchId(matchId)
        .alertedPartyId(alertedPartyId)
        .ofacId(ofacId)
        .watchlistType(watchlistType)
        .discriminator(getDiscriminator())
        .build();
  }
}
