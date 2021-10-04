package com.silenteight.payments.bridge.agents.port;

import com.silenteight.payments.bridge.agents.model.SpecificCommonTermsAgentResponse;
import com.silenteight.payments.bridge.agents.model.SpecificCommonTermsRequest;

public interface SpecificCommonTermsUseCase {

  SpecificCommonTermsAgentResponse invoke(SpecificCommonTermsRequest request);
}
