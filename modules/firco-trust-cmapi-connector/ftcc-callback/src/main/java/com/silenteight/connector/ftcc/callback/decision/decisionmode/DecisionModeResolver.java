package com.silenteight.connector.ftcc.callback.decision.decisionmode;

import javax.annotation.Nonnull;

public interface DecisionModeResolver {

  @Nonnull
  DecisionMode resolve(String unit);
}
