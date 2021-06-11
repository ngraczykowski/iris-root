package com.silenteight.serp.governance.qa.analysis.update;

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
import static com.silenteight.serp.governance.qa.domain.DecisionLevel.ANALYSIS;
import static java.time.OffsetDateTime.now;
import static org.springframework.http.ResponseEntity.accepted;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class UpdateAnalysisDecisionRestController {

  private static final String UPDATE_DECISION_URL = "/v1/qa/0/alerts/{id}";
  @NonNull
  private final UpdateAnalysisDecisionUseCase useCase;

  @PatchMapping(UPDATE_DECISION_URL)
  @PreAuthorize("isAuthorized('ALERTS_ANALYSIS')")
  @ResponseStatus
  public ResponseEntity<Void> update(@PathVariable UUID id,
      @RequestBody @Valid UpdateAnalysisDecisionDto updateAnalysisDecisionDto,
      Authentication authentication) {

    useCase.activate(UpdateDecisionRequest.of(
        toResourceName(id),
        updateAnalysisDecisionDto.getDecision(),
        ANALYSIS,
        updateAnalysisDecisionDto.getComment(),
        authentication.getName(),
        now()
    ));
    return accepted().build();
  }
}

