package com.silenteight.payments.bridge.firco.datasource.service.process;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.payments.bridge.agents.model.NameAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateNameFeatureInputUseCase;
import com.silenteight.payments.bridge.datasource.port.CreateAgentInputsClient;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import java.util.List;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.NAME_FEATURE;
import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.createFeatureInput;

class NameAgentEtlProcess extends BaseAgentEtlProcess<NameFeatureInput> {

  private final CreateNameFeatureInputUseCase createNameFeatureInputUseCase;

  NameAgentEtlProcess(
      CreateAgentInputsClient createAgentInputsClient,
      CreateNameFeatureInputUseCase createNameFeatureInputUseCase) {
    super(createAgentInputsClient);
    this.createNameFeatureInputUseCase = createNameFeatureInputUseCase;
  }

  @Override
  protected List<FeatureInput> createDataSourceFeatureInputs(HitData hitData) {
    var nameAgentUseCaseRequest = createNameAgentUseCaseRequest(hitData);
    var nameFeatureInput = createNameFeatureInputUseCase.create(nameAgentUseCaseRequest);
    var featureInput = createFeatureInput(NAME_FEATURE, nameFeatureInput);
    return List.of(featureInput);
  }

  private static NameAgentRequest createNameAgentUseCaseRequest(HitData hitData) {
    return NameAgentRequest.builder()
        .feature(NAME_FEATURE)
        .watchlistNames(List.of(hitData.getHitAndWlPartyData().getName()))
        .alertedPartyNames(hitData.getAlertedPartyData().getNames())
        .watchlistType(hitData.getHitAndWlPartyData().getWatchlistType())
        .matchingTexts(hitData.getHitAndWlPartyData().getAllMatchingTexts())
        .build();
  }
}
