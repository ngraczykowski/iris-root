package com.silenteight.serp.governance.policy.step.clone;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.accepted;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class CloneStepRestController {

  @NonNull
  private final CloneStepUseCase cloneStepUseCase;

  @PostMapping(value = "/v1/policies/{policyId}/steps/{stepId}/clone/steps/{newStepId}")
  @PreAuthorize("isAuthorized('CLONE_STEP')")
  public ResponseEntity<Void> cloneStep(
      @PathVariable UUID policyId,
      @PathVariable UUID stepId,
      @PathVariable UUID newStepId,
      Authentication authentication) {

    CloneStepCommand command = CloneStepCommand.builder()
        .newStepId(newStepId)
        .baseStepId(stepId)
        .policyId(policyId)
        .createdBy(authentication.getName())
        .build();

    cloneStepUseCase.activate(command);
    return accepted().build();
  }
}
