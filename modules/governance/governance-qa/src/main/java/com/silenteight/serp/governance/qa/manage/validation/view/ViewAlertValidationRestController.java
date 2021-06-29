package com.silenteight.serp.governance.qa.manage.validation.view;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.manage.common.AlertResource;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.VALIDATION;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class ViewAlertValidationRestController {

  private static final String ALERT_VIEWING_URL = "/v1/qa/1/alerts/{id}:viewing";
  @NonNull
  private final ViewAlertValidationUseCase viewAlertUseCase;

  @PostMapping(ALERT_VIEWING_URL)
  @PreAuthorize("isAuthorized('ALERTS_VALIDATION')")
  public ResponseEntity<Void> view(@PathVariable UUID id) {
    viewAlertUseCase.activate(
        ViewDecisionCommand.builder()
            .alertName(AlertResource.toResourceName(id))
            .level(VALIDATION)
            .build());
    return ResponseEntity.accepted().build();
  }
}

