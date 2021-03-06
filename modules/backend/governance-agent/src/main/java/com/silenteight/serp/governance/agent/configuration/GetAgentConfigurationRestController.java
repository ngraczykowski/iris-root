package com.silenteight.serp.governance.agent.configuration;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.agent.domain.AgentsRegistry;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.sep.auth.authentication.RestConstants.ROOT;
import static com.silenteight.serp.governance.agent.domain.DomainConstants.AGENT_ENDPOINT_TAG;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = AGENT_ENDPOINT_TAG)
class GetAgentConfigurationRestController {

  private static final String AGENTS_CONFIG_URL = "/v1/agents/{id}/configuration/{configurationId}";

  @NonNull
  private final AgentsRegistry agentsRegistry;

  @GetMapping(value = AGENTS_CONFIG_URL)
  @PreAuthorize("isAuthorized('LIST_AGENTS')")
  public ResponseEntity<Object> listConfig(
      @PathVariable String id, @PathVariable String configurationId) {

    log.info("Getting agent's configuration, agentId={}, configurationId={}", id, configurationId);
    Object configuration = agentsRegistry.getAgentConfigurationDetails(id, configurationId);
    return ok(configuration);
  }
}
