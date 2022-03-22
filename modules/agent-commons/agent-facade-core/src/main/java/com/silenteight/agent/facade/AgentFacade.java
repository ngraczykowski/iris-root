package com.silenteight.agent.facade;

public interface AgentFacade<T, R> {

  @SuppressWarnings("UnusedReturnValue")
  R processMessage(T message);

  default String getFacadeName() {
    return "";
  }
}
