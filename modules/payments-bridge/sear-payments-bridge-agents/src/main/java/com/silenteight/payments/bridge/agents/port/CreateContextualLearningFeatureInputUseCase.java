package com.silenteight.payments.bridge.agents.port;

import com.silenteight.datasource.api.historicaldecisions.v2.HistoricalDecisionsFeatureInput;
import com.silenteight.payments.bridge.agents.model.ContextualLearningAgentRequest;

public interface CreateContextualLearningFeatureInputUseCase {

  HistoricalDecisionsFeatureInput create(ContextualLearningAgentRequest nameAgentRequest);
}
