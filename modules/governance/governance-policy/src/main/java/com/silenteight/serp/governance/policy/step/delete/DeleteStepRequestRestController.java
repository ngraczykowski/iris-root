package com.silenteight.serp.governance.policy.step.delete;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.serp.governance.common.web.rest.RestConstants.ROOT;

@Slf4j
@RestController
@RequestMapping(ROOT)
@RequiredArgsConstructor
class DeleteStepRequestRestController {

  @NonNull
  private final DeleteStepUseCase removeStepUseCase;

  @DeleteMapping(value = "/v1/steps/{id}")
  @PreAuthorize("isAuthorized('REMOVE_STEP')")
  public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication authentication) {

    DeleteStepCommand command = DeleteStepCommand
        .builder()
        .id(id)
        .updatedBy(authentication.getName())
        .build();
    removeStepUseCase.activate(command);

    return ResponseEntity.noContent().build();
  }

}
