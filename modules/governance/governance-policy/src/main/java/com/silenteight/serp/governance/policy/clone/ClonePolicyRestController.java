package com.silenteight.serp.governance.policy.clone;

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

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class ClonePolicyRestController {

  @NonNull
  private final ClonePolicyUseCase clonePolicyUseCase;

  @PostMapping(value = "/v1/policies/{id}/clone/policies/{newId}")
  @PreAuthorize("isAuthorized('CLONE_POLICY')")
  public ResponseEntity<Void> clone(
      @PathVariable UUID id,
      @PathVariable UUID newId,
      Authentication authentication) {

    ClonePolicyCommand command = ClonePolicyCommand
        .builder()
        .id(newId)
        .createdBy(authentication.getName())
        .basePolicyId(id)
        .build();
    clonePolicyUseCase.activate(command);

    return ResponseEntity.accepted().build();
  }
}
