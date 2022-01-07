package com.silenteight.sens.webapp.user.roles.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.user.roles.edit.dto.EditRoleDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.ROLE_MANAGEMENT;
import static org.springframework.http.ResponseEntity.accepted;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ROOT)
class EditRoleRestController {

  @NonNull
  private final EditRoleUseCase editRoleUseCase;

  @PatchMapping("/v2/roles")
  @PreAuthorize("isAuthorized('EDIT_ROLE')")
  public ResponseEntity<Void> edit(
      @Valid @RequestBody EditRoleDto dto, Authentication authentication) {

    log.info(ROLE_MANAGEMENT, "Editing Role. dto={}", dto);

    String userName = authentication.getName();

    EditRoleCommand command = EditRoleCommand.builder()
        .id(dto.getId())
        .name(dto.getName())
        .description(dto.getDescription())
        .updatedBy(userName)
        .build();

    editRoleUseCase.activate(command);
    return accepted().build();
  }

  @PutMapping("/v2/roles/{id}/permissions")
  @PreAuthorize("isAuthorized('EDIT_ROLE')")
  public ResponseEntity<Void> assignPermissionsToRole(
      @PathVariable UUID id, @Valid @RequestBody List<UUID> permissions) {

    return accepted().build();
  }
}
