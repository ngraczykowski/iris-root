package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.payments.bridge.agents.model.ContextualLearningAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateContextualLearningFeatureInputUseCase;
import com.silenteight.payments.bridge.firco.datasource.model.DatasourceUnstructuredModel;
import com.silenteight.payments.bridge.svb.oldetl.response.HitAndWatchlistPartyData;

import java.util.List;

import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.createFeatureInput;

@RequiredArgsConstructor
abstract class ContextualLearningFeatureProcess implements CreateFeatureInputUnstructured {

  private final CreateContextualLearningFeatureInputUseCase createFeatureInput;

  //TODO(jgajewski): Remove when refactoring soon
  protected abstract String getFeature();

  protected abstract String getFeatureName();

  protected abstract String getDiscriminator();

  @Override
  public List<AgentInput> createFeatureInputs(DatasourceUnstructuredModel inputModel) {
    var dataSourceFeatureInputs = getFeatureInput(inputModel);
    var agentInput = AgentInput.newBuilder()
        .setAlert(inputModel.getAlertName())
        .setMatch(inputModel.getMatchName())
        .addFeatureInputs(dataSourceFeatureInputs)
        .build();
    return List.of(agentInput);
  }

  private FeatureInput getFeatureInput(
      DatasourceUnstructuredModel inputModel) {
    var request = createRequest(inputModel.getHitAndWatchlistPartyData());
    var historicalDecisionsFeatureInput = createFeatureInput.create(request);
    return createFeatureInput(getFeature(), historicalDecisionsFeatureInput);
  }

  private ContextualLearningAgentRequest createRequest(
      HitAndWatchlistPartyData hitAndWatchlistPartyData) {
    return hitAndWatchlistPartyData.contextualLearningAgentRequestBuilder()
        .feature(getFeatureName())
        .discriminator(getDiscriminator())
        .build();
  }
}
