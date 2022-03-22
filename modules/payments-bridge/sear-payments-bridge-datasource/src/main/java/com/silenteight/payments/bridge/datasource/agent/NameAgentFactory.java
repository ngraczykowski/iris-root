package com.silenteight.payments.bridge.datasource.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.agents.model.NameAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateNameFeatureInputUseCase;
import com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured.NameAgentData;

import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.NAME_FEATURE_NAME;

@Component
@RequiredArgsConstructor
class NameAgentFactory extends BaseFeatureInputStructuredFactory {

  private final CreateNameFeatureInputUseCase createNameFeatureInputUseCase;

  @Override
  protected FeatureInput createFeatureInput(FeatureInputStructured featureInputStructured) {
    var nameAgentUseCaseRequest =
        createNameAgentUseCaseRequest(featureInputStructured.getNameAgentData());
    var nameFeatureInput = createNameFeatureInputUseCase.createDefault(nameAgentUseCaseRequest);
    return AgentDataSourceUtils.createFeatureInput(NAME_FEATURE_NAME, nameFeatureInput);
  }

  private static NameAgentRequest createNameAgentUseCaseRequest(NameAgentData nameAgentData) {
    return NameAgentRequest.builder()
        .feature(NAME_FEATURE_NAME)
        .watchlistNames(nameAgentData.getWatchlistPartyName())
        .alertedPartyNames(nameAgentData.getAlertedPartyNames())
        .watchlistType(nameAgentData.getWatchlistType())
        .matchingTexts(nameAgentData.getMatchingTexts())
        .build();
  }
}
