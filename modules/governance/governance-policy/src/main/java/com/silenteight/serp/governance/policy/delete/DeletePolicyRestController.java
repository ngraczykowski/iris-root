package com.silenteight.serp.governance.policy.delete;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ACCEPTED_STATUS;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.SUCCESS_RESPONSE_DESCRIPTION;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.POLICY_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.accepted;

@Slf4j
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

    log.info("Deleting policy. policyId={}", id);

    DeletePolicyCommand command = DeletePolicyCommand.of(id, authentication.getName());
    deletePolicyUseCase.activate(command);

    log.debug("Deleting policy request processed.");
    return accepted().build();
  }
}
