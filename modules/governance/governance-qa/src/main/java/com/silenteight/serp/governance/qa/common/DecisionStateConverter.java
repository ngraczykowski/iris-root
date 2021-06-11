package com.silenteight.serp.governance.qa.common;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.silenteight.serp.governance.qa.domain.DecisionState;

import java.util.List;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DecisionStateConverter {

  public static List<String> asStringList(List<DecisionState> decisionStates) {
    return decisionStates
        .stream()
        .map(Enum::toString)
        .collect(toList());
  }
}