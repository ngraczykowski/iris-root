package com.silenteight.payments.bridge.agents.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.payments.bridge.agents.model.SpecificTermsAgentResponse;
import com.silenteight.payments.bridge.agents.model.SpecificTermsRequest;
import com.silenteight.payments.bridge.agents.port.SpecificTerms2UseCase;

import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

@RequiredArgsConstructor
class SpecificTerms2Agent implements SpecificTerms2UseCase {

  private final List<Mapping> mappings;

  @NonNull
  public SpecificTermsAgentResponse invoke(SpecificTermsRequest request) {
    for (Mapping mapping : mappings) {
      if (mapping.matches(request.getAllMatchFieldsValue()))
        return new SpecificTermsAgentResponse(mapping.response);
    }

    return new SpecificTermsAgentResponse("NO");
  }

  static class Mapping {

    private final Pattern pattern;
    private final String response;

    Mapping(List<String> patterns, String response) {
      pattern = Pattern.compile(String.join("|", patterns));
      this.response = response;
    }

    private boolean matches(String allMatchFieldsValue) {
      var input = allMatchFieldsValue.toUpperCase(Locale.ROOT);
      var matcher = pattern.matcher(input);
      return matcher.find();
    }
  }
}
