package com.silenteight.sens.webapp.sso.delete;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ACCEPTED_STATUS;
import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.common.rest.RestConstants.SUCCESS_RESPONSE_DESCRIPTION;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.SSO_MANAGEMENT;
import static com.silenteight.sens.webapp.sso.domain.DomainConstants.SSO_ENDPOINT_TAG;
import static org.springframework.http.ResponseEntity.noContent;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ROOT)
@Tag(name = SSO_ENDPOINT_TAG)
class DeleteSsoMappingRestController {

  @NonNull
  private final DeleteSsoMappingUseCase deleteSsoMappingUseCase;

  @DeleteMapping("/sso/mappings/{id}")
  @PreAuthorize("isAuthorized('DELETE_SSO_MAPPING')")
  @ApiResponses(value = {
      @ApiResponse(responseCode = ACCEPTED_STATUS, description = SUCCESS_RESPONSE_DESCRIPTION)
  })
  public ResponseEntity<Void> delete(@PathVariable UUID id) {
    log.info(SSO_MANAGEMENT, "Deleting sso mapping id={}", id);

    DeleteSsoMappingRequest request = DeleteSsoMappingRequest.builder()
        .ssoMappingId(id)
        .build();

    deleteSsoMappingUseCase.activate(request);
    log.debug("Sso mapping deleted.");
    return noContent().build();
  }
}
