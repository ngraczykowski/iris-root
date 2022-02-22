package com.silenteight.sens.webapp.role.remove;

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

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.ROLE_MANAGEMENT;
import static org.springframework.http.ResponseEntity.noContent;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ROOT)
class RemoveRoleRestController {

  @NonNull
  private final RemoveRoleUseCase removeRoleUseCase;

  @DeleteMapping(value = "/v2/roles/{id}")
  @PreAuthorize("isAuthorized('DELETE_ROLE')")
  public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication authentication) {
    log.info(ROLE_MANAGEMENT, "Removing role. roleId={}", id);

    RemoveRoleRequest command = RemoveRoleRequest.builder()
        .id(id)
        .deletedBy(authentication.getName())
        .build();

    removeRoleUseCase.activate(command);
    log.info(ROLE_MANAGEMENT, "Role removed.");
    return noContent().build();
  }
}
