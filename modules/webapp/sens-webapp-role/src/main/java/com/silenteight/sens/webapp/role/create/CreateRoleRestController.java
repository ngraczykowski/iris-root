package com.silenteight.sens.webapp.role.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.role.create.dto.CreateRoleDto;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.silenteight.sens.webapp.common.rest.RestConstants.*;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.ROLE_MANAGEMENT;
import static com.silenteight.sens.webapp.role.domain.DomainConstants.ROLE_ENDPOINT_TAG;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ROOT)
@Tag(name = ROLE_ENDPOINT_TAG)
class CreateRoleRestController {

  @NonNull
  private final CreateRoleUseCase createRoleUseCase;

  @PostMapping("/v2/roles")
  @PreAuthorize("isAuthorized('CREATE_ROLE')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = CREATED_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION),
      @ApiResponse(responseCode = BAD_REQUEST_STATUS, description = BAD_REQUEST_DESCRIPTION)
  })
  public ResponseEntity<Void> create(
      @RequestBody @Valid CreateRoleDto dto, Authentication authentication) {

    log.info(ROLE_MANAGEMENT, "Creating new role. dto={}", dto);

    CreateRoleRequest command = CreateRoleRequest.builder()
        .id(dto.getId())
        .name(dto.getName())
        .description(dto.getDescription())
        .permissions(dto.getPermissions())
        .createdBy(authentication.getName())
        .build();

    createRoleUseCase.activate(command);
    log.info(ROLE_MANAGEMENT, "Role created.");
    return status(CREATED).build();
  }
}
