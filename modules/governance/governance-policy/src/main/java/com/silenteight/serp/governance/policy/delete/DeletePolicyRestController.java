package com.silenteight.serp.governance.policy.delete;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ACCEPTED_STATUS;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.SUCCESS_RESPONSE_DESCRIPTION;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.POLICY_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.accepted;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Tag(name = POLICY_ENDPOINT_TAG)
class DeletePolicyRestController {

  @NonNull
  private final DeletePolicyUseCase deletePolicyUseCase;

  @DeleteMapping(value = "/v1/policies/{id}")
  @PreAuthorize("isAuthorized('DELETE_POLICY')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = ACCEPTED_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION)
  })
  public ResponseEntity<Void> delete(
      @PathVariable UUID id, Authentication authentication) {

    DeletePolicyCommand command = DeletePolicyCommand.of(id, authentication.getName());
    deletePolicyUseCase.activate(command);
    return accepted().build();
  }
}
