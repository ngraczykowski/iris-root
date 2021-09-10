package com.silenteight.payments.bridge.agents.service;

import lombok.NonNull;

import com.silenteight.payments.bridge.agents.model.SanctionedNationalityAgentResponse;
import com.silenteight.payments.bridge.agents.port.SanctionedNationalityUseCase;

import org.springframework.stereotype.Service;

import java.util.List;

import static com.silenteight.payments.bridge.agents.model.SanctionedNationalityAgentResponse.NO;
import static com.silenteight.payments.bridge.agents.model.SanctionedNationalityAgentResponse.YES;

@Service
class SanctionedNationalityAgent implements SanctionedNationalityUseCase {

  @NonNull
  public SanctionedNationalityAgentResponse invoke(
      String inputStr, List<String> sanctionedNationalities) {
    for (String sanctionedNationality : sanctionedNationalities) {
      if (inputStr.contains(sanctionedNationality))
        return YES;
    }

    return NO;
  }

}
