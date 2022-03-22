package com.silenteight.agent.facade.exception;

import static java.lang.String.format;

public class NoFeatureSolutionException extends RuntimeException {

  private static final long serialVersionUID = 685220418551975418L;

  public NoFeatureSolutionException(String agentName) {
    super(format("No feature solution found in %s Agent", agentName));
  }
}
