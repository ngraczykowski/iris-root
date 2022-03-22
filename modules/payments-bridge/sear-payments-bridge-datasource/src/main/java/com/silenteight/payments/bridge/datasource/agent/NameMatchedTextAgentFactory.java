package com.silenteight.payments.bridge.datasource.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.agents.model.NameAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateNameFeatureInputUseCase;
import com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputUnstructured;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputUnstructured.NameMatchedTextAgent;

import org.springframework.stereotype.Component;

import java.util.List;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.NAME_FEATURE_NAME;
import static com.silenteight.payments.bridge.common.app.AgentsUtils.NAME_TEXT_FEATURE_NAME;

@Component
@RequiredArgsConstructor
class NameMatchedTextAgentFactory extends BaseFeatureInputUnstructuredFactory {

  private final CreateNameFeatureInputUseCase createNameFeatureInputUseCase;

  @Override
  protected FeatureInput createFeatureInput(FeatureInputUnstructured featureInputUnstructured) {
    var nameMatchedTextAgentData =
        featureInputUnstructured.getNameMatchedTextAgentData();
    var nameMatchedTextRequest = createNameAgentUseCaseRequest(nameMatchedTextAgentData);
    var nameFeatureInput = createNameFeatureInputUseCase.createDefault(nameMatchedTextRequest);
    return AgentDataSourceUtils.createFeatureInput(NAME_FEATURE_NAME, nameFeatureInput);
  }

  private static NameAgentRequest createNameAgentUseCaseRequest(
      NameMatchedTextAgent nameMatchedTextAgent) {
    return NameAgentRequest.builder()
        .feature(NAME_TEXT_FEATURE_NAME)
        .watchlistNames(List.of(nameMatchedTextAgent.getWatchlistName()))
        .alertedPartyNames(nameMatchedTextAgent.getAlertedPartyName())
        .watchlistType(nameMatchedTextAgent.getWatchlistType())
        .matchingTexts(nameMatchedTextAgent.getMatchingTexts())
        .build();
  }
}
