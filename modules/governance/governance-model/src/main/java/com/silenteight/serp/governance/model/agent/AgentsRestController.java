package com.silenteight.serp.governance.model.agent;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class AgentsRestController {

  @NonNull
  private final AgentMappingService agentMappingService;

  @GetMapping(value = "/v1/features")
  @PreAuthorize("isAuthorized('LIST_AGENTS')")
  public ResponseEntity<FeaturesListDto> getFeaturesListDto() {
    return ResponseEntity.ok().body(agentMappingService.getFeaturesListDto());
  }
}
