package com.silenteight.payments.bridge.agents;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
class SpecificTermsAgent {

  @NonNull
  private final List<String> specificTerms;

  @NonNull
  public SpecificTermsAgentResponse invoke(@NonNull String allMatchFieldsValue) {
    return checkIfFieldValueContainsSpecificTerms(allMatchFieldsValue);
  }

  SpecificTermsAgentResponse checkIfFieldValueContainsSpecificTerms(@NonNull String fieldValue) {

    Pattern pattern = Pattern.compile(String.join("|", specificTerms));
    Matcher matcher = pattern.matcher(fieldValue.toUpperCase(Locale.ROOT));

    if (matcher.find()) {
      return SpecificTermsAgentResponse.YES;
    } else {
      return SpecificTermsAgentResponse.NO;
    }
  }
}
