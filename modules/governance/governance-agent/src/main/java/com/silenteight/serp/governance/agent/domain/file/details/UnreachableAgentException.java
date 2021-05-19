package com.silenteight.serp.governance.agent.domain.file.details;

import com.silenteight.serp.governance.agent.domain.file.config.AgentConfigDto;

public class UnreachableAgentException extends NonResolvableResourceException {

  private static final long serialVersionUID = -7990934467471467352L;


  public UnreachableAgentException(String id) {
    super(String.format("Configuration for the following agentId=%s, cannot be obtained", id));
  }

  UnreachableAgentException(AgentConfigDto agentConfigDto) {
    super("Configuration for the following agent cannot be obtained: " + agentConfigDto.getName());
  }
}
