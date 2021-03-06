package com.silenteight.serp.governance.agent.list;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.agent.list.dto.ListAgentDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static com.silenteight.sep.auth.authentication.RestConstants.ROOT;
import static com.silenteight.serp.governance.agent.domain.DomainConstants.AGENT_ENDPOINT_TAG;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = AGENT_ENDPOINT_TAG)
public class ListAgentsRestController {

  private static final String LIST_AGENTS_URL = "/v1/agents";

  private final ListAgentQuery listAgentQuery;

  @GetMapping(LIST_AGENTS_URL)
  @PreAuthorize("isAuthorized('LIST_AGENTS')")
  public ResponseEntity<Collection<ListAgentDto>> list() {
    log.info("Listing all agents.");
    return ResponseEntity.ok(listAgentQuery.list());
  }
}
