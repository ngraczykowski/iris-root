package com.silenteight.serp.governance.agent.details;

import com.silenteight.serp.governance.agent.NonResolvableResourceException;
import com.silenteight.serp.governance.agent.config.AgentConfigDto;

class UnreachableAgentException extends NonResolvableResourceException {

  private static final long serialVersionUID = -7990934467471467352L;

  UnreachableAgentException(AgentConfigDto agentConfigDto) {
    super("Configuration for the following agent cannot be obtained: " + agentConfigDto.getName());
  }
}
