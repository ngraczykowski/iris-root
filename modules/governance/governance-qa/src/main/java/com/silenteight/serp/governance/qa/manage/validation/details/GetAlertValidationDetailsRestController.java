package com.silenteight.serp.governance.qa.manage.validation.details;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.manage.validation.details.dto.AlertValidationDetailsDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static com.silenteight.serp.governance.qa.manage.common.AlertResource.toResourceName;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class GetAlertValidationDetailsRestController {

  private static final String ALERT_DETAILS_URL = "/v1/qa/1/alerts/{alertId}";

  @NonNull
  private final AlertDetailsQuery alertDetailsQuery;

  @GetMapping(ALERT_DETAILS_URL)
  @PreAuthorize("isAuthorized('ALERTS_VALIDATION')")
  public ResponseEntity<AlertValidationDetailsDto> details(@PathVariable String alertId) {
    return ok(alertDetailsQuery.details(toResourceName(alertId)));
  }
}

