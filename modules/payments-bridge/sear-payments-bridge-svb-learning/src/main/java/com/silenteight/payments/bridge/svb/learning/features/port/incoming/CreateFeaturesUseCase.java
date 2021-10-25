package com.silenteight.payments.bridge.svb.learning.features.port.incoming;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.payments.bridge.svb.learning.reader.domain.LearningAlert;

import java.util.List;

public interface CreateFeaturesUseCase {

  List<AgentInput> createMatchFeatures(LearningAlert learningAlert);

  void createMatchFeatures(List<LearningAlert> learningAlerts);

}
