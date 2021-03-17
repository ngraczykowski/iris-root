package com.silenteight.serp.governance.model.agent.details;

import com.silenteight.serp.governance.model.NonResolvableResourceException;
import com.silenteight.serp.governance.model.agent.config.AgentConfigDto;

class UnreachableAgentException extends NonResolvableResourceException {

  private static final long serialVersionUID = 2693591950239931843L;

  UnreachableAgentException(AgentConfigDto agentConfigDto) {
    super("Configuration for the following agent cannot be obtained: " + agentConfigDto.getName());
  }
}
