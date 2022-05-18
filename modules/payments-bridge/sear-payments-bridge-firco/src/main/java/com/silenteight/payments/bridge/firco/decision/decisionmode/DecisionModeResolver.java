package com.silenteight.payments.bridge.firco.decision.decisionmode;

import javax.annotation.Nonnull;

public interface DecisionModeResolver {

  @Nonnull
  DecisionMode resolve(String unit);
}
