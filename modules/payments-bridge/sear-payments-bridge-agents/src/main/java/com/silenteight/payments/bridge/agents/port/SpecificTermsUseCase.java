package com.silenteight.payments.bridge.agents.port;

import com.silenteight.payments.bridge.agents.model.SpecificTermsAgentResponse;
import com.silenteight.payments.bridge.agents.model.SpecificTermsRequest;

public interface SpecificTermsUseCase {

  SpecificTermsAgentResponse invoke(SpecificTermsRequest request);
}
