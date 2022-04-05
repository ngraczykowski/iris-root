package com.silenteight.connector.ftcc.callback.newdecision;

import lombok.Getter;
import lombok.NonNull;

import java.util.List;

@Getter
class DecisionConfigurationHolder {

  private final List<DecisionTransition> decisionTransitions;

  DecisionConfigurationHolder(@NonNull List<DecisionTransition> decisionTransitions) {
    if (decisionTransitions.isEmpty()) {
      throw new IllegalStateException("DecisionTransition data is empty!!!");
    }
    this.decisionTransitions = decisionTransitions;
  }
}
