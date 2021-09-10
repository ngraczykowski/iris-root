package com.silenteight.payments.bridge.agents.port;

import lombok.NonNull;

import com.silenteight.payments.bridge.agents.model.SpecificCommonTermsAgentResponse;

public interface SpecificCommonTermsUseCase {

  SpecificCommonTermsAgentResponse invoke(
      @NonNull String allMatchFieldsValue, @NonNull Boolean isAccountNumberFlagInMatchingField);
}
