package com.silenteight.payments.bridge.agents.port;

import lombok.NonNull;

import com.silenteight.payments.bridge.agents.model.SpecificTermsAgentResponse;

import java.util.List;

public interface SpecificTermsUseCase {

  SpecificTermsAgentResponse invoke(
      @NonNull String allMatchFieldsValue, List<String> specificTerms);
}
