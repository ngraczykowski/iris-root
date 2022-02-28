package com.silenteight.serp.governance.policy.clone;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

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

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ACCEPTED_STATUS;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.SUCCESS_RESPONSE_DESCRIPTION;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.UNPROCESSABLE_ENTITY_STATUS;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.POLICY_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.accepted;

@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Tag(name = POLICY_ENDPOINT_TAG)
class ClonePolicyRestController {

  @NonNull
  private final ClonePolicyUseCase clonePolicyUseCase;

  @PostMapping(value = "/v1/policies/{id}/clone/policies/{newId}")
  @PreAuthorize("isAuthorized('CLONE_POLICY')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = ACCEPTED_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = UNPROCESSABLE_ENTITY_STATUS, description = "policy not exists")
  })
  public ResponseEntity<Void> clone(
      @PathVariable UUID id,
      @PathVariable UUID newId,
      Authentication authentication) {

    ClonePolicyCommand command = ClonePolicyCommand.builder()
        .id(newId)
        .createdBy(authentication.getName())
        .basePolicyId(id)
        .build();
    clonePolicyUseCase.activate(command);
    return accepted().build();
  }
}
