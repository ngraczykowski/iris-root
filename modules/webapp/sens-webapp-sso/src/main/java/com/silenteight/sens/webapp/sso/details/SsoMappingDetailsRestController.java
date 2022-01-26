package com.silenteight.sens.webapp.sso.details;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sens.webapp.sso.list.dto.SsoMappingDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.SSO_MANAGEMENT;
import static org.springframework.http.ResponseEntity.*;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ROOT)
class SsoMappingDetailsRestController {

  @NonNull
  SsoMappingDetailsQuery ssoMappingDetailsQuery;

  @GetMapping("/sso-mappings/{name}")
  @PreAuthorize("isAuthorized('LIST_SSO_MAPPINGS')")
  public ResponseEntity<SsoMappingDto> details(@PathVariable String name) {
    log.info(SSO_MANAGEMENT, "Getting details sso mapping name={}", name);
    return ok(ssoMappingDetailsQuery.details(name));
  }
}
