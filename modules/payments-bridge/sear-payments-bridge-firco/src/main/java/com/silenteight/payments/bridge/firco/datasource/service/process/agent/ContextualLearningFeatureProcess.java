package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.agents.model.ContextualLearningAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateContextualLearningFeatureInputUseCase;
import com.silenteight.payments.bridge.firco.datasource.model.DatasourceUnstructuredModel;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;

import org.springframework.stereotype.Component;

import java.util.List;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.CONTEXTUAL_LEARNING_FEATURE;
import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.createFeatureInput;

@Component
@RequiredArgsConstructor
class ContextualLearningFeatureProcess implements CreateFeatureInputUnstructured {

  private final CreateContextualLearningFeatureInputUseCase createFeatureInput;

  @Override
  public List<AgentInput> createFeatureInputs(DatasourceUnstructuredModel inputModel) {
    var dataSourceFeatureInputs =
        createDataSourceFeatureInputs(inputModel.getHitAndWatchlistPartyData());

    var agentInput = AgentInput.newBuilder()
        .setAlert(inputModel.getAlertName())
        .setMatch(inputModel.getMatchName())
        .addFeatureInputs(dataSourceFeatureInputs)
        .build();
    return List.of(agentInput);
  }

  private FeatureInput createDataSourceFeatureInputs(
      HitAndWatchlistPartyData hitAndWatchlistPartyData) {
    var request = creteRequest(hitAndWatchlistPartyData);
    var historicalDecisionsFeatureInput = createFeatureInput.create(request);
    return createFeatureInput(CONTEXTUAL_LEARNING_FEATURE, historicalDecisionsFeatureInput);
  }

  protected static ContextualLearningAgentRequest creteRequest(
      HitAndWatchlistPartyData hitAndWatchlistPartyData) {
    return hitAndWatchlistPartyData.contextualLearningAgentRequest();
  }
}
