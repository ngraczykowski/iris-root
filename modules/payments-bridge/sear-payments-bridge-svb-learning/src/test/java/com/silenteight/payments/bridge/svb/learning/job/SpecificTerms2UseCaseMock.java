package com.silenteight.payments.bridge.svb.learning.job;

import com.silenteight.payments.bridge.agents.model.SpecificTermsAgentResponse;
import com.silenteight.payments.bridge.agents.model.SpecificTermsRequest;
import com.silenteight.payments.bridge.agents.port.SpecificTerms2UseCase;

public class SpecificTerms2UseCaseMock implements SpecificTerms2UseCase {

  @Override
  public SpecificTermsAgentResponse invoke(SpecificTermsRequest request) {
    return new SpecificTermsAgentResponse("response");
  }
}
