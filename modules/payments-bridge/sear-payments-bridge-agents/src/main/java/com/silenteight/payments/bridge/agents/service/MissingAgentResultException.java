package com.silenteight.payments.bridge.agents.service;

class MissingAgentResultException extends IllegalStateException {

  private static final long serialVersionUID = -5020599302447794996L;

  MissingAgentResultException(String agentName) {
    super(agentName + " has not returned any result.");
  }
}
