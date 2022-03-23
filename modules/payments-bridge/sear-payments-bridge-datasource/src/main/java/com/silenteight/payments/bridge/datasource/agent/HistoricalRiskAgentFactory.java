package com.silenteight.payments.bridge.datasource.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.agents.model.HistoricalRiskAssessmentAgentFeatureRequest;
import com.silenteight.payments.bridge.agents.port.HistoricalRiskAssessmentFeatureUseCase;
import com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured.HistoricalAgentData;

import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
abstract class HistoricalRiskAgentFactory extends BaseFeatureInputStructuredFactory {

  private final HistoricalRiskAssessmentFeatureUseCase historicalRiskAssessmentFeatureAgent;

  @Override
  protected FeatureInput createFeatureInput(FeatureInputStructured featureInputStructured) {

    var historicalAgentData = featureInputStructured.getHistoricalAgentData();
    var request = createHistoricalRiskAgentRequest(historicalAgentData);
    var featureInput = historicalRiskAssessmentFeatureAgent.invoke(request);
    return AgentDataSourceUtils.createFeatureInput(getFeatureName(), featureInput);
  }

  private HistoricalRiskAssessmentAgentFeatureRequest createHistoricalRiskAgentRequest(
      HistoricalAgentData historicalAgentData) {
    return HistoricalRiskAssessmentAgentFeatureRequest.builder()
        .feature(getFeatureName())
        .matchId(historicalAgentData.getMatchId())
        .alertedPartyId(getAlertedPartyId(historicalAgentData.getAccountNumber()))
        .ofacId(historicalAgentData.getOfacId())
        .watchlistType(historicalAgentData.getWatchlistType().toString())
        .discriminator(getDiscriminator())
        .build();
  }

  protected static String getAlertedPartyId(String accountNumber) {
    return StringUtils.isBlank(accountNumber) ? "N/A" : accountNumber.toUpperCase().trim();
  }

  protected abstract String getDiscriminator();

}
