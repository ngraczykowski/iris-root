package com.silenteight.payments.bridge.svb.newlearning.step.etl.feature.service;

import lombok.RequiredArgsConstructor;

import com.silenteight.datasource.agentinput.api.v1.FeatureInput;
import com.silenteight.datasource.api.historicaldecisions.v2.HistoricalDecisionsFeatureInput;
import com.silenteight.payments.bridge.agents.model.ContextualLearningAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateContextualLearningFeatureInputUseCase;
import com.silenteight.payments.bridge.svb.newlearning.domain.HitComposite;

import org.springframework.stereotype.Service;

import static com.silenteight.payments.bridge.common.app.AgentsUtils.CONTEXTUAL_LEARNING_FEATURE;
import static com.silenteight.payments.bridge.common.protobuf.AgentDataSourceUtils.createFeatureInput;

@Service
@RequiredArgsConstructor
class ContextualLearningFeatureExtractorService implements UnstructuredFeatureExtractor {

  private final CreateContextualLearningFeatureInputUseCase createFeatureInput;

  @Override
  public FeatureInput createFeatureInputs(HitComposite hit) {
    HistoricalDecisionsFeatureInput featureInput = getHistoricalDecisionsFeatureInput(hit);
    return createFeatureInput(CONTEXTUAL_LEARNING_FEATURE, featureInput);
  }

  private HistoricalDecisionsFeatureInput getHistoricalDecisionsFeatureInput(HitComposite hit) {
    var request = getRequest(hit);
    return createFeatureInput.create(request);
  }

  private static ContextualLearningAgentRequest getRequest(HitComposite hit) {
    return hit.createContextualLearningAgentRequest();
  }
}
