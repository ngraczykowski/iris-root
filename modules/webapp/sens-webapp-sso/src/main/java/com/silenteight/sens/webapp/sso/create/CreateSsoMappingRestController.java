package com.silenteight.sens.webapp.sso.create;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.sso.create.dto.CreateSsoMappingDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.SSO_MANAGEMENT;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ROOT)
class CreateSsoMappingRestController {

  @NonNull
  private final CreateSsoMappingUseCase createSsoMappingUseCase;

  @PostMapping("/sso/mappings")
  @PreAuthorize("isAuthorized('CREATE_SS0_MAPPING')")
  public ResponseEntity<Void> create(@Valid @RequestBody CreateSsoMappingDto dto) {
    log.info(SSO_MANAGEMENT, "Creating new sso mapping. dto={}", dto);

    CreateSsoMappingCommand command = CreateSsoMappingCommand.builder()
        .name(dto.getName())
        .roles(dto.getRoles())
        .attributes(dto.getAttributes())
        .build();

    createSsoMappingUseCase.activate(command);
    return status(CREATED).build();
  }
}
