package com.silenteight.serp.governance.qa.manage.analysis.details;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.manage.analysis.details.dto.AlertAnalysisDetailsDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class GetAnalysisAlertDetailsRestController {

  private static final String ALERT_DETAILS_URL = "/v1/qa/0/alerts/{discriminator}";

  @NonNull
  private final AlertDetailsQuery detailsQuery;

  @GetMapping(ALERT_DETAILS_URL)
  @PreAuthorize("isAuthorized('ALERTS_ANALYSIS')")
  public ResponseEntity<AlertAnalysisDetailsDto> details(@PathVariable String discriminator) {
    return ok(detailsQuery.details(discriminator));
  }
}

