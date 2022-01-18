package com.silenteight.sens.webapp.sso.identityproviders.list;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.silenteight.sep.usermanagement.api.dto.IdentityProviderDto;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

import static com.silenteight.sens.webapp.common.rest.RestConstants.ROOT;
import static com.silenteight.sens.webapp.logging.SensWebappLogMarkers.SSO_MANAGEMENT;
import static org.springframework.http.ResponseEntity.ok;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(ROOT)
class ListIdentityProvidersRestController {

  @NonNull
  private final ListIdentityProvidersQuery listIdentityProvidersQuery;

  @GetMapping("/identity-providers")
  @PreAuthorize("isAuthorized('LIST_IDENTITY_PROVIDERS')")
  public ResponseEntity<Collection<IdentityProviderDto>> list() {
    log.info(SSO_MANAGEMENT, "Listing identity providers.");
    return ok(listIdentityProvidersQuery.listAll());
  }
}
