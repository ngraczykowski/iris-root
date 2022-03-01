package com.silenteight.payments.bridge.svb.learning.step.etl.feature.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.historicaldecisions.v2.HistoricalDecisionsFeatureInput;
import com.silenteight.payments.bridge.agents.model.ContextualLearningAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateContextualLearningFeatureInputUseCase;
import com.silenteight.payments.bridge.svb.learning.domain.HitComposite;

import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.createFeatureInput;

@RequiredArgsConstructor
abstract class ContextualLearningFeatureExtractorService implements UnstructuredFeatureExtractor {

  private final CreateContextualLearningFeatureInputUseCase createFeatureInput;

  //TODO(jgajewski): Remove soon while refactoring
  protected abstract String getFeature();

  protected abstract String getFeatureName();

  protected abstract String getDiscriminator();

  @Override
  public FeatureInput createFeatureInputs(HitComposite hit) {
    HistoricalDecisionsFeatureInput featureInput = getHistoricalDecisionsFeatureInput(hit);
    return createFeatureInput(getFeature(), featureInput);
  }

  private HistoricalDecisionsFeatureInput getHistoricalDecisionsFeatureInput(HitComposite hit) {
    var request = getRequest(hit);
    return createFeatureInput.create(request);
  }

  private ContextualLearningAgentRequest getRequest(HitComposite hit) {
    return hit.createContextualAgentRequestBuilder()
        .feature(getFeatureName())
        .discriminator(getDiscriminator())
        .build();
  }
}
