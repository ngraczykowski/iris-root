package com.silenteight.payments.bridge.agents.port;

import com.silenteight.payments.bridge.agents.model.CompanyNameSurroundingAgentResponse;
import com.silenteight.payments.bridge.agents.model.CompanyNameSurroundingRequest;

public interface CompanyNameSurroundingUseCase {

  CompanyNameSurroundingAgentResponse invoke(CompanyNameSurroundingRequest request);
}
