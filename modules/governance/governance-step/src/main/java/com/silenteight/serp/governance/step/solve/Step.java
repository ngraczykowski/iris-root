package com.silenteight.serp.governance.step.solve;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
class Step {

  private final SolutionWithStepId solutionWithStep;
  private final Map<Integer, List<String>> stepLogic;

  boolean matchValues(List<String> values) {
    return stepLogic
        .entrySet()
        .stream()
        .allMatch(entry -> match(entry, values));
  }

  private static boolean match(Map.Entry<Integer, List<String>> entry, List<String> values) {
    int key = entry.getKey();

    if (values.size() > key)
      return entry.getValue().contains(values.get(key));
    else
      return false;
  }

  SolutionWithStepId getSolutionWithStepId() {
    return solutionWithStep;
  }
}
