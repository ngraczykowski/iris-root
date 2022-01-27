package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.agents.model.NameAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateNameFeatureInputUseCase;
import com.silenteight.payments.bridge.firco.datasource.model.FeatureInputUnstructuredModel;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.createFeatureInput;

@Component
@RequiredArgsConstructor
class NameMatchedTextAgentEtlProcess implements CreateFeatureInputUnstructured {

  private static final String NAME_MATCH_TEXT_FEATURE = "nameMatchedText";

  private final CreateNameFeatureInputUseCase createNameFeatureInputUseCase;

  @Override
  public List<AgentInput> createFeatureInputs(
      FeatureInputUnstructuredModel inputModel) {
    var dataSourceFeatureInputs =
        createDataSourceFeatureInputs(inputModel.getHitAndWatchlistPartyData());
    return dataSourceFeatureInputs.stream()
        .map(featureInput -> AgentInput.newBuilder()
            .setAlert(inputModel.getAlertName())
            .setMatch(inputModel.getMatchName())
            .addFeatureInputs(featureInput)
            .build())
        .collect(Collectors.toList());
  }

  protected List<FeatureInput> createDataSourceFeatureInputs(
      HitAndWatchlistPartyData hitAndWatchlistPartyData) {
    var nameAgentUseCaseRequest = createNameAgentUseCaseRequest(hitAndWatchlistPartyData);
    var nameFeatureInput = createNameFeatureInputUseCase.create(nameAgentUseCaseRequest);
    var featureInput = createFeatureInput(NAME_MATCH_TEXT_FEATURE, nameFeatureInput);
    return List.of(featureInput);
  }

  private static NameAgentRequest createNameAgentUseCaseRequest(
      HitAndWatchlistPartyData hitAndWatchlistPartyData) {
    return NameAgentRequest.builder()
        .feature(NAME_MATCH_TEXT_FEATURE)
        .watchlistNames(List.of(hitAndWatchlistPartyData.getName()))
        .alertedPartyNames(List.of(hitAndWatchlistPartyData.getMatchingText()))
        .watchlistType(hitAndWatchlistPartyData.getWatchlistType())
        .matchingTexts(hitAndWatchlistPartyData.getAllMatchingTexts())
        .build();
  }
}
