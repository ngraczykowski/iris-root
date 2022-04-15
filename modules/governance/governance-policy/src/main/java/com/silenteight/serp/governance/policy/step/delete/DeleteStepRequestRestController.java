package com.silenteight.serp.governance.policy.step.delete;

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

import static com.silenteight.serp.governance.common.web.rest.RestConstants.NO_CONTENT_STATUS;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;
import static com.silenteight.serp.governance.common.web.rest.RestConstants.SUCCESS_RESPONSE_DESCRIPTION;
import static com.silenteight.serp.governance.policy.domain.DomainConstants.POLICY_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.*;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
@Tag(name = POLICY_ENDPOINT_TAG)
class DeleteStepRequestRestController {

  @NonNull
  private final DeleteStepUseCase removeStepUseCase;

  @DeleteMapping("/v1/steps/{id}")
  @PreAuthorize("isAuthorized('REMOVE_STEP')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = NO_CONTENT_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION)
  })
  public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication authentication) {
    log.info("Deleting step. stepId={}", id);

    DeleteStepCommand command = DeleteStepCommand
        .builder()
        .id(id)
        .updatedBy(authentication.getName())
        .build();
    removeStepUseCase.activate(command);

    log.debug("Deleting step request processed.");
    return noContent().build();
  }
}
