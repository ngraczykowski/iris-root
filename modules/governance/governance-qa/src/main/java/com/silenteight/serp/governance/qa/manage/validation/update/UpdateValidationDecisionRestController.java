package com.silenteight.serp.governance.qa.manage.validation.update;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.manage.domain.dto.UpdateDecisionRequest;
import com.silenteight.serp.governance.qa.manage.validation.update.dto.UpdateValidationDecisionDto;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.*;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.NOT_FOUND_DESCRIPTION;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.NOT_FOUND_STATUS;
import static com.silenteight.serp.governance.qa.manage.common.AlertResource.toResourceName;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.VALIDATION;
import static com.silenteight.serp.governance.qa.manage.domain.DomainConstants.QA_ENDPOINT_TAG;
import static java.time.OffsetDateTime.now;
import static org.springframework.http.ResponseEntity.accepted;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Tag(name = QA_ENDPOINT_TAG)
class UpdateValidationDecisionRestController {

  private static final String UPDATE_VALIDATION_DECISION_URL = "/v1/qa/1/alerts/{alertId}";

  @NonNull
  private final UpdateValidationDecisionUseCase decisionUseCase;

  @PatchMapping(UPDATE_VALIDATION_DECISION_URL)
  @PreAuthorize("isAuthorized('ALERTS_VALIDATION_DECISION')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = ACCEPTED_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = BAD_REQUEST_STATUS, description = BAD_REQUEST_DESCRIPTION),
      @ApiResponse(responseCode = NOT_FOUND_STATUS, description = NOT_FOUND_DESCRIPTION)
  })
  public ResponseEntity<Void> edit(
      @PathVariable String alertId,
      @RequestBody @Valid UpdateValidationDecisionDto updateValidationDecisionDto,
      Authentication authentication) {

    UpdateDecisionRequest request = UpdateDecisionRequest.of(
        toResourceName(alertId),
        updateValidationDecisionDto.getState(),
        VALIDATION,
        updateValidationDecisionDto.getComment(),
        authentication.getName(),
        now()
    );
    decisionUseCase.activate(request);
    return accepted().build();
  }
}

