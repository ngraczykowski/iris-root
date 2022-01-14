package com.silenteight.payments.bridge.svb.newlearning.job;

import com.silenteight.payments.bridge.agents.model.SpecificTermsAgentResponse;
import com.silenteight.payments.bridge.agents.model.SpecificTermsRequest;
import com.silenteight.payments.bridge.agents.port.SpecificTermsUseCase;

public class SpecificTermsUseCaseMock implements SpecificTermsUseCase {

  @Override
  public SpecificTermsAgentResponse invoke(SpecificTermsRequest request) {
    return new SpecificTermsAgentResponse("YES");
  }
}
