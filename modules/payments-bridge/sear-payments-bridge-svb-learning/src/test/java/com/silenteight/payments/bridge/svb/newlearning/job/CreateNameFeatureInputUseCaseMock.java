package com.silenteight.payments.bridge.svb.newlearning.job;

import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.payments.bridge.agents.model.NameAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateNameFeatureInputUseCase;

class CreateNameFeatureInputUseCaseMock implements CreateNameFeatureInputUseCase {

  @Override
  public NameFeatureInput createDefault(NameAgentRequest nameAgentRequest) {
    return NameFeatureInput.getDefaultInstance();
  }

  @Override
  public NameFeatureInput createForOrganizationNameAgent(NameAgentRequest nameAgentRequest) {
    return NameFeatureInput.getDefaultInstance();
  }
}
