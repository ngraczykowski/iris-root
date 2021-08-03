package com.silenteight.hsbc.bridge.alert;

import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
class AnalystDecisionMapper {

  private static final Map<String, String> ANALYST_DECISION_MAP = createMap();
  private static final String UNKNOWN = "analyst_decision_unknown";

  String getAnalystDecision(String currentState) {
    if (ANALYST_DECISION_MAP.containsKey(currentState)) {
      return ANALYST_DECISION_MAP.get(currentState);
    }
    return UNKNOWN;
  }

  private static Map<String, String> createMap() {
    final String TRUE_POSITIVE = "analyst_decision_true_positive";
    final String FALSE_POSITIVE = "analyst_decision_false_positive";
    final String PENDING = "analyst_decision_pending";

    var map = new HashMap<String, String>();

    map.put("True Match Exit Completed", TRUE_POSITIVE);
    map.put("True Match Retained", TRUE_POSITIVE);
    map.put("PEP True Match", TRUE_POSITIVE);
    map.put("True Match Exit / Retained Recommended", TRUE_POSITIVE);

    map.put("False Positive", FALSE_POSITIVE);

    map.put("Level 1 Review", PENDING);
    map.put("Level 2 Review", PENDING);
    map.put("Level 3 Review", PENDING);
    map.put("Isolated Level 3", PENDING);
    map.put("Isolated Level 1 / 2", PENDING);

    return map;
  }
}
