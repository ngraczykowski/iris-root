package com.silenteight.serp.governance.qa.manage.analysis.view;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.*;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.NOT_FOUND_DESCRIPTION;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.NOT_FOUND_STATUS;
import static com.silenteight.serp.governance.qa.manage.common.AlertResource.toResourceName;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.ANALYSIS;
import static com.silenteight.serp.governance.qa.manage.domain.DomainConstants.QA_ENDPOINT_TAG;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Tag(name = QA_ENDPOINT_TAG)
class ViewAlertAnalysisRestController {

  private static final String ALERT_VIEW_URL = "/v1/qa/0/alerts/{alertId}:viewing";

  @NonNull
  private final ViewAlertAnalysisUseCase viewAlertUseCase;

  @PostMapping(ALERT_VIEW_URL)
  @PreAuthorize("isAuthorized('ALERTS_ANALYSIS')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = ACCEPTED_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = BAD_REQUEST_STATUS, description = BAD_REQUEST_DESCRIPTION),
      @ApiResponse(responseCode = NOT_FOUND_STATUS, description = NOT_FOUND_DESCRIPTION)
  })
  public ResponseEntity<Void> view(@PathVariable String alertId) {
    viewAlertUseCase.activate(
        ViewDecisionCommand.builder()
            .alertName(toResourceName(alertId))
            .level(ANALYSIS)
            .build());
    return ResponseEntity.accepted().build();
  }
}

