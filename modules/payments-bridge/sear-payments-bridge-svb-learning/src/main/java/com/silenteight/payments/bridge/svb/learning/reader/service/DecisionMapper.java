package com.silenteight.payments.bridge.svb.learning.reader.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
class DecisionMapper {

  @NonNull
  private final Map<String, String> decisionMap;

  @NonNull
  private final String defaultValue;

  public String map(String input) {
    return decisionMap.getOrDefault(input, defaultValue);
  }
}
