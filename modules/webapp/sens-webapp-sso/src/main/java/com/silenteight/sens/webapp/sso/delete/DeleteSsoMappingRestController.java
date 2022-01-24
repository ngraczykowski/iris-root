package com.silenteight.sens.webapp.sso.delete;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.SSO_MANAGEMENT;
import static org.springframework.http.ResponseEntity.accepted;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ROOT)
class DeleteSsoMappingRestController {

  @NonNull
  private final DeleteSsoMappingUseCase deleteSsoMappingUseCase;

  @DeleteMapping("/sso-mappings/{name}")
  @PreAuthorize("isAuthorized('DELETE_SSO_MAPPING')")
  public ResponseEntity<Void> delete(@PathVariable String name) {
    log.info(SSO_MANAGEMENT, "Deleting sso mapping name={}", name);
    DeleteSsoMappingRequest request = DeleteSsoMappingRequest.builder()
        .ssoMappingName(name)
        .build();

    deleteSsoMappingUseCase.activate(request);
    return accepted().build();
  }
}
