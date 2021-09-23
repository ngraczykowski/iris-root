package com.silenteight.universaldatasource.app.feature.port.incoming;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.datasource.agentinput.api.v1.BatchCreateAgentInputsResponse;

import java.util.Collection;

public interface BatchCreateMatchFeaturesUseCase {

  BatchCreateAgentInputsResponse batchCreateMatchFeatures(Collection<AgentInput> requests);
}
