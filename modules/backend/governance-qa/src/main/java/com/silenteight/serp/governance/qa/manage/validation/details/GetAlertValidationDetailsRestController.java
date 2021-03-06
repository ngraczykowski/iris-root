package com.silenteight.serp.governance.qa.manage.validation.details;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.serp.governance.qa.manage.validation.details.dto.AlertValidationDetailsDto;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.sep.auth.authentication.RestConstants.*;
import static com.silenteight.serp.governance.qa.manage.common.AlertResource.toResourceName;
import static com.silenteight.serp.governance.qa.manage.domain.DomainConstants.QA_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.ok;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
@Tag(name = QA_ENDPOINT_TAG)
class GetAlertValidationDetailsRestController {

  private static final String ALERT_DETAILS_URL = "/v1/qa/1/alerts/{alertId}";

  @NonNull
  private final AlertDetailsQuery alertDetailsQuery;

  @GetMapping(ALERT_DETAILS_URL)
  @PreAuthorize("isAuthorized('ALERTS_VALIDATION')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = OK_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = BAD_REQUEST_STATUS, description = BAD_REQUEST_DESCRIPTION,
          content = @Content)
  })
  public ResponseEntity<AlertValidationDetailsDto> details(@PathVariable String alertId) {
    log.info("Getting alert validatioan details for alertId={}", alertId);
    return ok(alertDetailsQuery.details(toResourceName(alertId)));
  }
}
