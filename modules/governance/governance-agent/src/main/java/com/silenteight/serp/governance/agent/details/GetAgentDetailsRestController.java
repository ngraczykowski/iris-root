package com.silenteight.serp.governance.agent.details;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.agent.details.dto.AgentDetailsDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.serp.governance.agent.domain.DomainConstants.AGENT_ENDPOINT_TAG;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = AGENT_ENDPOINT_TAG)
class GetAgentDetailsRestController {

  private static final String AGENT_URL = "/v1/agents/{id}";

  private final AgentDetailsQuery agentDetailsQuery;

  @GetMapping(AGENT_URL)
  @PreAuthorize("isAuthorized('LIST_AGENTS')")
  public ResponseEntity<AgentDetailsDto> getAgentDetails(@PathVariable String id) {
    log.info("Getting details for agent, agentId={}", id);
    return ok(agentDetailsQuery.details(id));
  }
}
