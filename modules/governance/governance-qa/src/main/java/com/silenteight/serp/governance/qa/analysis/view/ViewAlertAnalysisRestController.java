package com.silenteight.serp.governance.qa.analysis.view;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.common.AlertResource;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static com.silenteight.serp.governance.qa.domain.DecisionLevel.ANALYSIS;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class ViewAlertAnalysisRestController {

  private static final String ALERT_VIEW_URL = "/v1/qa/0/alerts/{id}:viewing";

  @NonNull
  private final ViewAlertAnalysisUseCase viewAlertUseCase;

  @PostMapping(ALERT_VIEW_URL)
  @PreAuthorize("isAuthorized('ALERTS_ANALYSIS')")
  public ResponseEntity<Void> view(@PathVariable UUID id) {
    viewAlertUseCase.activate(
        ViewDecisionCommand.builder()
            .alertName(AlertResource.toResourceName(id))
            .level(ANALYSIS)
            .build());
    return ResponseEntity.accepted().build();
  }
}

