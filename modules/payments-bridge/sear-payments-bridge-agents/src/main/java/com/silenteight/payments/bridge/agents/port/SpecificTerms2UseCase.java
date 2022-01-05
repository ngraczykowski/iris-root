package com.silenteight.payments.bridge.agents.port;

import com.silenteight.payments.bridge.agents.model.SpecificTermsAgentResponse;
import com.silenteight.payments.bridge.agents.model.SpecificTermsRequest;

public interface SpecificTerms2UseCase {

  SpecificTermsAgentResponse invoke(SpecificTermsRequest request);
}
