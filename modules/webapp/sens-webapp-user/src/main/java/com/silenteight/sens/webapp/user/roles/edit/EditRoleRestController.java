package com.silenteight.sens.webapp.user.roles.edit;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.user.roles.edit.dto.EditRoleDto;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ACCEPTED_STATUS;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.common.rest.RestConstants.SUCCESS_RESPONSE_DESCRIPTION;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.ROLE_MANAGEMENT;
import static com.silenteight.sens.webapp.role.domain.DomainConstants.ROLE_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.accepted;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ROOT)
@Tag(name = ROLE_ENDPOINT_TAG)
class EditRoleRestController {

  @NonNull
  private final EditRoleUseCase editRoleUseCase;

  @PatchMapping("/v2/roles")
  @PreAuthorize("isAuthorized('EDIT_ROLE')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = ACCEPTED_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION)
  })
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
  @ApiResponses(value = {
      @ApiResponse(responseCode = ACCEPTED_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION)
  })
  public ResponseEntity<Void> assignPermissionsToRole(
      @PathVariable UUID id, @Valid @RequestBody List<UUID> permissions) {

    return accepted().build();
  }
}
