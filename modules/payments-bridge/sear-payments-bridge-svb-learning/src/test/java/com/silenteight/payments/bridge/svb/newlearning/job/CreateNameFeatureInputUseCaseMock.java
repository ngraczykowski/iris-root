package com.silenteight.payments.bridge.svb.newlearning.job;

import com.silenteight.datasource.api.name.v1.NameFeatureInput;
import com.silenteight.payments.bridge.agents.model.NameAgentRequest;
import com.silenteight.payments.bridge.agents.port.CreateNameFeatureInputUseCase;

class CreateNameFeatureInputUseCaseMock implements CreateNameFeatureInputUseCase {

  @Override
  public NameFeatureInput create(NameAgentRequest nameAgentRequest) {
    return NameFeatureInput.getDefaultInstance();
  }
}
