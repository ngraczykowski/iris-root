package com.silenteight.sens.webapp.sso.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.sso.list.dto.SsoMappingDto;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.SSO_MANAGEMENT;
import static com.silenteight.sens.webapp.sso.domain.DomainConstants.SSO_ENDPOINT_TAG;
import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = ROOT, produces = APPLICATION_JSON_VALUE)
@Tag(name = SSO_ENDPOINT_TAG)
class ListSsoMappingRestController {

  @NonNull
  ListSsoMappingsQuery listSsoMappingsQuery;

  @GetMapping("/sso/mappings")
  @PreAuthorize("isAuthorized('LIST_SSO_MAPPINGS')")
  public ResponseEntity<Collection<SsoMappingDto>> list() {
    log.info(SSO_MANAGEMENT, "Listing sso mappings");
    return ResponseEntity.ok(listSsoMappingsQuery.listAll());
  }
}
