package com.silenteight.sens.webapp.backend.rest;

import lombok.RequiredArgsConstructor;

import com.silenteight.sens.webapp.backend.RestConstants;
import com.silenteight.sens.webapp.backend.presentation.dto.settings.DecisionConfig;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import static java.util.Collections.emptyList;

@RequiredArgsConstructor
@RequestMapping(RestConstants.ROOT)
public class FrontendSettingsController {

  @GetMapping("/settings/frontend")
  public ResponseEntity<Void> frontendSettings() {
    return ResponseEntity.ok().build();
  }

  @GetMapping("/settings/decisions")
  public ResponseEntity<List<DecisionConfig>> getDecisions() {
    return ResponseEntity.ok(emptyList());
  }
}
