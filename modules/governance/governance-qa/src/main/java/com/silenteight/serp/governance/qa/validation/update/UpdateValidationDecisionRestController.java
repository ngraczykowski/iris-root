package com.silenteight.serp.governance.qa.validation.update;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import com.silenteight.serp.governance.qa.analysis.update.dto.UpdateAnalysisDecisionDto;
import com.silenteight.serp.governance.qa.domain.dto.UpdateDecisionRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static com.silenteight.serp.governance.qa.common.AlertResource.toResourceName;
import static com.silenteight.serp.governance.qa.domain.DecisionLevel.VALIDATION;
import static java.time.OffsetDateTime.now;
import static org.springframework.http.ResponseEntity.accepted;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class UpdateValidationDecisionRestController {

  private static final String UPDATE_VALIDATION_DECISION_URL = "/v1/qa/1/alerts/{id}";

  @NonNull
  private final UpdateValidationDecisionUseCase decisionUseCase;

  @PatchMapping(UPDATE_VALIDATION_DECISION_URL)
  @PreAuthorize("isAuthorized('ALERTS_VALIDATION_DECISION')")
  public ResponseEntity<Void> edit(@PathVariable UUID id,
      @RequestBody @Valid UpdateAnalysisDecisionDto updateAnalysisDecisionDto,
      Authentication authentication) {

    UpdateDecisionRequest request = UpdateDecisionRequest.of(
        toResourceName(id),
        updateAnalysisDecisionDto.getDecision(),
        VALIDATION,
        updateAnalysisDecisionDto.getComment(),
        authentication.getName(),
        now()
    );
    decisionUseCase.activate(request);
    return accepted().build();
  }
}

