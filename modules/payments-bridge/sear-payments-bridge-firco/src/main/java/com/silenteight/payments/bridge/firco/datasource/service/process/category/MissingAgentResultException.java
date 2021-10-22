package com.silenteight.payments.bridge.firco.datasource.service.process.category;

class MissingAgentResultException extends IllegalStateException {

  private static final long serialVersionUID = -5020599302447794996L;

  MissingAgentResultException(String agentName) {
    super(agentName + " has not returned any result.");
  }
}
