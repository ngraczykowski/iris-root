package com.silenteight.payments.bridge.agents.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.model.SpecificTermsAgentResponse;
import com.silenteight.payments.bridge.agents.port.SpecificTermsUseCase;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.silenteight.payments.bridge.agents.model.SpecificTermsAgentResponse.NO;
import static com.silenteight.payments.bridge.agents.model.SpecificTermsAgentResponse.YES;

@RequiredArgsConstructor
@Service
class SpecificTermsAgent implements SpecificTermsUseCase {

  @NonNull
  public SpecificTermsAgentResponse invoke(
      @NonNull String allMatchFieldsValue, List<String> specificTerms) {

    Pattern pattern = Pattern.compile(String.join("|", specificTerms));
    Matcher matcher = pattern.matcher(allMatchFieldsValue.toUpperCase(Locale.ROOT));

    return matcher.find() ? YES : NO;
  }
}
