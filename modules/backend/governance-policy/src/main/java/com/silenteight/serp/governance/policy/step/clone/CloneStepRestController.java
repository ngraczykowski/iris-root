package com.silenteight.serp.governance.policy.step.clone;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.sep.auth.authentication.RestConstants.*;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.POLICY_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.accepted;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Tag(name = POLICY_ENDPOINT_TAG)
class CloneStepRestController {

  @NonNull
  private final CloneStepUseCase cloneStepUseCase;

  @PostMapping(value = "/v1/policies/{policyId}/steps/{stepId}/clone/steps/{newStepId}")
  @PreAuthorize("isAuthorized('CLONE_STEP')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = ACCEPTED_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = NOT_FOUND_STATUS, description = NOT_FOUND_DESCRIPTION)
  })
  public ResponseEntity<Void> cloneStep(
      @PathVariable UUID policyId,
      @PathVariable UUID stepId,
      @PathVariable UUID newStepId,
      Authentication authentication) {

    log.info("Cloning step. policyId={},stepId={},newStepId={}", policyId, stepId, newStepId);

    CloneStepCommand command = CloneStepCommand.builder()
        .newStepId(newStepId)
        .baseStepId(stepId)
        .policyId(policyId)
        .createdBy(authentication.getName())
        .build();

    cloneStepUseCase.activate(command);

    log.debug("Cloning step request processed.");
    return accepted().build();
  }
}
