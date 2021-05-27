package com.silenteight.adjudication.engine.analysis.agentresponse;

import java.util.Collection;

public class UnexpectedFeaturesReceivedException extends IllegalArgumentException {

  private static final long serialVersionUID = -4658475950677830434L;

  UnexpectedFeaturesReceivedException(Collection<String> unexpectedFeatures) {
    super("Received unexpected features in agent exchange response: [" + String.join(", ",
        unexpectedFeatures) + "].");
  }
}
