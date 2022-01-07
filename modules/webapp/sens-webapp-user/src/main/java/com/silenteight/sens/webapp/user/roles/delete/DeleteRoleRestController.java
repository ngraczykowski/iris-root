package com.silenteight.sens.webapp.user.roles.delete;

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
import static org.springframework.http.ResponseEntity.accepted;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ROOT)
class DeleteRoleRestController {

  @NonNull
  private final DeleteRoleUseCase deleteRoleUseCase;

  @DeleteMapping(value = "/v2/roles/{id}")
  @PreAuthorize("isAuthorized('DELETE_ROLE')")
  public ResponseEntity<Void> delete(@PathVariable UUID id, Authentication authentication) {
    log.info(ROLE_MANAGEMENT, "Deleting role roleId={}", id);
    String userName = authentication.getName();
    DeleteRoleCommand command = DeleteRoleCommand.builder()
        .id(id)
        .deletedBy(userName)
        .build();

    deleteRoleUseCase.activate(command);
    return accepted().build();
  }
}
