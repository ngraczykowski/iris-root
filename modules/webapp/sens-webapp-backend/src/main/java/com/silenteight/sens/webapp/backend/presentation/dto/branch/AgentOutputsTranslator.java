package com.silenteight.sens.webapp.backend.presentation.dto.branch;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
public class AgentOutputsTranslator {

  private final Map<String, String> translations;

  public String translate(String featureValue) {
    return translations.getOrDefault(featureValue, featureValue);
  }
}
