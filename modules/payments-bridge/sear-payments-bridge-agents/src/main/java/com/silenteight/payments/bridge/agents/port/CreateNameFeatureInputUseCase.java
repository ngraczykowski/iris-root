package com.silenteight.payments.bridge.agents.port;

import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.payments.bridge.agents.model.NameAgentRequest;

public interface CreateNameFeatureInputUseCase {

  NameFeatureInput createDefault(NameAgentRequest nameAgentRequest);

  NameFeatureInput createForOrganizationNameAgent(NameAgentRequest nameAgentRequest);
}
