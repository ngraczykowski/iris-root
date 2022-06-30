package com.silenteight.agent.facade;

public class AgentRequestTimeoutException extends RuntimeException {

  private static final long serialVersionUID = -2063797149347381587L;

  public AgentRequestTimeoutException(long timestamp) {
    super("Agent Request deadline time (" + timestamp + ")  exceeded");
  }
}
