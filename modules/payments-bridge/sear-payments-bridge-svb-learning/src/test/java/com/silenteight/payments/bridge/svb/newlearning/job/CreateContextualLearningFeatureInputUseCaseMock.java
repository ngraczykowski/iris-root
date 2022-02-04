package com.silenteight.payments.bridge.svb.newlearning.job;

import com.silenteight.datasource.api.historicaldecisions.v2.HistoricalDecisionsFeatureInput;
import com.silenteight.payments.bridge.agents.model.ContextualLearningAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateContextualLearningFeatureInputUseCase;

class CreateContextualLearningFeatureInputUseCaseMock implements
    CreateContextualLearningFeatureInputUseCase {

  @Override
  public HistoricalDecisionsFeatureInput create(ContextualLearningAgentRequest request) {
    return HistoricalDecisionsFeatureInput.getDefaultInstance();
  }
}
