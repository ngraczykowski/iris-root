package com.silenteight.payments.bridge.agents.port;

import com.silenteight.payments.bridge.agents.model.SanctionedNationalityAgentResponse;

import java.util.List;

public interface SanctionedNationalityUseCase {

  SanctionedNationalityAgentResponse invoke(
      String inputStr, List<String> sanctionedNationalities);
}
