package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.payments.bridge.firco.datasource.model.DatasourceUnstructuredModel;

import java.util.List;

public interface CreateFeatureInputUnstructured {

  List<AgentInput> createFeatureInputs(DatasourceUnstructuredModel inputModel);

}
