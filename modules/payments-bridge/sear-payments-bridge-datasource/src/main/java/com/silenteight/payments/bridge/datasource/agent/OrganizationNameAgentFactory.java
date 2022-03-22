package com.silenteight.payments.bridge.datasource.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.agents.model.NameAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateNameFeatureInputUseCase;
import com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured;
import com.silenteight.payments.bridge.datasource.agent.dto.FeatureInputStructured.NameAgentData;

import org.springframework.stereotype.Component;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.ORGANIZATION_NAME_FEATURE_NAME;

@Component
@RequiredArgsConstructor
class OrganizationNameAgentFactory extends BaseFeatureInputStructuredFactory {

  private final CreateNameFeatureInputUseCase createNameFeatureInputUseCase;

  @Override
  protected FeatureInput createFeatureInput(FeatureInputStructured featureInputStructured) {
    var nameAgentUseCaseRequest =
        createNameAgentUseCaseRequest(featureInputStructured.getNameAgentData());
    var nameFeatureInput =
        createNameFeatureInputUseCase.createForOrganizationNameAgent(nameAgentUseCaseRequest);
    return AgentDataSourceUtils.createFeatureInput(
        ORGANIZATION_NAME_FEATURE_NAME, nameFeatureInput);
  }

  private static NameAgentRequest createNameAgentUseCaseRequest(NameAgentData nameAgentData) {
    return NameAgentRequest.builder()
        .feature(ORGANIZATION_NAME_FEATURE_NAME)
        .alertedPartyNames(nameAgentData.getAlertedPartyNames())
        .watchlistNames(nameAgentData.getWatchlistPartyName())
        .watchlistType(nameAgentData.getWatchlistType())
        .build();

  }
}
