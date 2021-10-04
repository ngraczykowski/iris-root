package com.silenteight.payments.bridge.agents.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.model.SanctionedNationalityAgentResponse;
import com.silenteight.payments.bridge.agents.port.SanctionedNationalityUseCase;

import java.util.List;

import static com.silenteight.payments.bridge.agents.model.SanctionedNationalityAgentResponse.NO;
import static com.silenteight.payments.bridge.agents.model.SanctionedNationalityAgentResponse.YES;

@RequiredArgsConstructor
class SanctionedNationalityAgent implements SanctionedNationalityUseCase {

  private final List<String> sanctionedNationalities;

  @NonNull
  public SanctionedNationalityAgentResponse invoke(String inputStr) {
    for (String sanctionedNationality : sanctionedNationalities) {
      if (inputStr.contains(sanctionedNationality))
        return YES;
    }

    return NO;
  }

}
