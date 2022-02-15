package com.silenteight.serp.governance.qa.manage.analysis.view;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static com.silenteight.serp.governance.qa.manage.common.AlertResource.toResourceName;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.ANALYSIS;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class ViewAlertAnalysisRestController {

  private static final String ALERT_VIEW_URL = "/v1/qa/0/alerts/{alertId}:viewing";

  @NonNull
  private final ViewAlertAnalysisUseCase viewAlertUseCase;

  @PostMapping(ALERT_VIEW_URL)
  @PreAuthorize("isAuthorized('ALERTS_ANALYSIS')")
  public ResponseEntity<Void> view(@PathVariable String alertId) {
    viewAlertUseCase.activate(
        ViewDecisionCommand.builder()
            .alertName(toResourceName(alertId))
            .level(ANALYSIS)
            .build());
    return ResponseEntity.accepted().build();
  }
}

