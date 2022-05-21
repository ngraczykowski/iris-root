package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;

import java.util.Map;

@RequiredArgsConstructor
class AnalystDecisionMapper {

  private static final String UNKNOWN = "UNKNOWN";
  private final Map<String, String> analystDecisionMap;

  String getAnalystDecision(String currentState) {
    if (analystDecisionMap.containsKey(currentState)) {
      return analystDecisionMap.get(currentState);
    } else {
      return getUnknownValue();
    }
  }

  private String getUnknownValue() {
    return analystDecisionMap.get(UNKNOWN);
  }
}
