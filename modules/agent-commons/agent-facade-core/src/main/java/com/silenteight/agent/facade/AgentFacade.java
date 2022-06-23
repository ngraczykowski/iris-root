package com.silenteight.agent.facade;

import java.util.Set;

public interface AgentFacade<T, R> {

  @SuppressWarnings("UnusedReturnValue")
  R processMessage(T message, Set<String> configNames);

  default String getFacadeName() {
    return "";
  }
}
