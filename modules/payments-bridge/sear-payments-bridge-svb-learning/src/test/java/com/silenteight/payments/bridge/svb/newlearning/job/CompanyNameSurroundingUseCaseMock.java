package com.silenteight.payments.bridge.svb.newlearning.job;

import com.silenteight.payments.bridge.agents.model.CompanyNameSurroundingAgentResponse;
import com.silenteight.payments.bridge.agents.model.CompanyNameSurroundingRequest;
import com.silenteight.payments.bridge.agents.port.CompanyNameSurroundingUseCase;

class CompanyNameSurroundingUseCaseMock implements CompanyNameSurroundingUseCase {

  @Override
  public CompanyNameSurroundingAgentResponse invoke(CompanyNameSurroundingRequest request) {
    return CompanyNameSurroundingAgentResponse.AGENT_ERROR;
  }
}
