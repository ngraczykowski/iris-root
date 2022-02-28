package com.silenteight.serp.governance.qa.manage.analysis.update;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.manage.analysis.update.dto.UpdateAnalysisDecisionDto;
import com.silenteight.serp.governance.qa.manage.domain.dto.UpdateDecisionRequest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.*;
import static com.silenteight.serp.governance.qa.manage.common.AlertResource.toResourceName;
import static com.silenteight.serp.governance.qa.manage.domain.DecisionLevel.ANALYSIS;
import static com.silenteight.serp.governance.qa.manage.domain.DomainConstants.QA_ENDPOINT_TAG;
import static java.time.OffsetDateTime.now;
import static org.springframework.http.ResponseEntity.accepted;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Tag(name = QA_ENDPOINT_TAG)
class UpdateAnalysisDecisionRestController {

  private static final String UPDATE_DECISION_URL = "/v1/qa/0/alerts/{alertId}";
  @NonNull
  private final UpdateAnalysisDecisionUseCase useCase;

  @PatchMapping(UPDATE_DECISION_URL)
  @PreAuthorize("isAuthorized('ALERTS_ANALYSIS_DECISION')")
  @ResponseStatus
  @ApiResponses(value = {
      @ApiResponse(responseCode = ACCEPTED_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = BAD_REQUEST_STATUS, description = BAD_REQUEST_DESCRIPTION),
      @ApiResponse(responseCode = NOT_FOUND_STATUS, description = NOT_FOUND_DESCRIPTION)
  })
  public ResponseEntity<Void> update(@PathVariable String alertId,
      @RequestBody @Valid UpdateAnalysisDecisionDto updateAnalysisDecisionDto,
      Authentication authentication) {

    useCase.activate(UpdateDecisionRequest.of(
        toResourceName(alertId),
        updateAnalysisDecisionDto.getState(),
        ANALYSIS,
        updateAnalysisDecisionDto.getComment(),
        authentication.getName(),
        now()
    ));
    return accepted().build();
  }
}

