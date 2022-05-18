package com.silenteight.payments.bridge.svb.learning.mapping;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@Slf4j
class DecisionEntry {

  private final DecisionKey decisionKey;
  private final String decision;

  DecisionEntry(String[] inputLine) {
    if (inputLine.length != 3) {
      log.error("Decision input line: {} is expected to contain 3 values",
          String.join(",", inputLine));
      throw new IllegalArgumentException("Invalid decision mapping configuration");
    }

    decisionKey = new DecisionKey(inputLine[0].trim(), inputLine[1].trim());
    decision = inputLine[2].trim();
  }

  boolean isDefaultCase() {
    return "*".equals(decisionKey.getPrevious()) && "*".equals(decisionKey.getCurrent());
  }

  @Value
  static class DecisionKey {
    String previous;
    String current;
  }

}
