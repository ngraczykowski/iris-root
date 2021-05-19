package com.silenteight.serp.governance.agent.list;

import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.agent.list.dto.ListAgentDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
public class ListAgentsRestController {

  private static final String LIST_AGENTS_URL = "/v1/agents";

  private final ListAgentQuery listAgentQuery;

  @GetMapping(LIST_AGENTS_URL)
  @PreAuthorize("isAuthorized('LIST_AGENTS')")
  public ResponseEntity<Collection<ListAgentDto>> list() {
    return ResponseEntity.ok(listAgentQuery.list());
  }
}
