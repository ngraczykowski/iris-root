package com.silenteight.payments.bridge.agents.port;

import com.silenteight.payments.bridge.agents.model.SanctionedNationalityAgentResponse;

public interface SanctionedNationalityUseCase {

  SanctionedNationalityAgentResponse invoke(String inputStr);
}
