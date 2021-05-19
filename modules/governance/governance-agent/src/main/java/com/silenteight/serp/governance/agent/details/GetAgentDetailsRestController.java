package com.silenteight.serp.governance.agent.details;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.agent.details.dto.AgentDetailsDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class GetAgentDetailsRestController {

  private static final String AGENT_URL = "/v1/agents/{id}";

  private final AgentDetailsQuery agentDetailsQuery;

  @GetMapping(AGENT_URL)
  @PreAuthorize("isAuthorized('LIST_AGENTS')")
  public ResponseEntity<AgentDetailsDto> getAgentDetails(@PathVariable String id) {
    return ok(agentDetailsQuery.details(id));
  }
}
