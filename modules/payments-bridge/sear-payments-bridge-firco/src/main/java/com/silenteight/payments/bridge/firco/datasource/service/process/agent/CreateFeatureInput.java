package com.silenteight.payments.bridge.firco.datasource.service.process.agent;

import com.silenteight.datasource.agentinput.api.v1.AgentInput;
import com.silenteight.payments.bridge.common.model.AeAlert;
import com.silenteight.payments.bridge.firco.datasource.model.DatasourceUnstructuredModel;
import com.silenteight.payments.bridge.svb.oldetl.response.HitData;

import java.util.List;

public interface CreateFeatureInput {

  List<AgentInput> createStructuredFeatureInputs(AeAlert alert, List<HitData> hitsData);

  List<AgentInput> createUnstructuredFeatureInputs(DatasourceUnstructuredModel featureInputModel);

}
