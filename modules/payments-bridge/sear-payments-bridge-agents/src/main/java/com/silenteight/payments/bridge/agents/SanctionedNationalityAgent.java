package com.silenteight.payments.bridge.agents;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
class SanctionedNationalityAgent {

  @NonNull
  private final List<String> sanctionedNationalities;

  @NonNull
  SanctionedNationalityAgentResponse invoke(@NonNull String inputStr) {
    for (String sanctionedNationality : sanctionedNationalities) {
      if (inputStr.contains(sanctionedNationality))
        return SanctionedNationalityAgentResponse.YES;
    }

    return SanctionedNationalityAgentResponse.NO;
  }

}
