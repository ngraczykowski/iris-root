package com.silenteight.payments.bridge.agents.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.model.SpecificTermsAgentResponse;
import com.silenteight.payments.bridge.agents.model.SpecificTermsRequest;
import com.silenteight.payments.bridge.agents.port.SpecificTermsUseCase;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.silenteight.payments.bridge.agents.model.SpecificTermsAgentResponse.NO;
import static com.silenteight.payments.bridge.agents.model.SpecificTermsAgentResponse.YES;

@RequiredArgsConstructor
class SpecificTermsAgent implements SpecificTermsUseCase {

  private final Pattern pattern;

  SpecificTermsAgent(List<String> specificTerms) {
    pattern = Pattern.compile(String.join("|", specificTerms));
  }

  @NonNull
  public SpecificTermsAgentResponse invoke(SpecificTermsRequest request) {
    Matcher matcher = pattern.matcher(request.getAllMatchFieldsValue().toUpperCase(Locale.ROOT));

    return matcher.find() ? YES : NO;
  }
}
