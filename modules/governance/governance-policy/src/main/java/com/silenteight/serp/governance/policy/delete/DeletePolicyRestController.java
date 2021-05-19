package com.silenteight.serp.governance.policy.delete;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static org.springframework.http.ResponseEntity.accepted;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class DeletePolicyRestController {

  @NonNull
  private final DeletePolicyUseCase deletePolicyUseCase;

  @DeleteMapping(value = "/v1/policies/{id}")
  @PreAuthorize("isAuthorized('DELETE_POLICY')")
  public ResponseEntity<Void> delete(
      @PathVariable UUID id, Authentication authentication) {

    DeletePolicyCommand command = DeletePolicyCommand.of(id, authentication.getName());
    deletePolicyUseCase.activate(command);
    return accepted().build();
  }
}
